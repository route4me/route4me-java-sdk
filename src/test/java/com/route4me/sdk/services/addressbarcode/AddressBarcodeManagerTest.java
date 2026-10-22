package com.route4me.sdk.services.addressbarcode;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link AddressBarcodeManager}: reading and saving destination barcodes on
 * the V5 API.
 */
public class AddressBarcodeManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String BARCODES_EP = "/modules/api/v5.0/address-barcodes";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private AddressBarcodeManager manager() {
        return http.install(new AddressBarcodeManager(MockHttp.API_KEY));
    }

    @Test
    public void getAddressBarcodesSendsOnlyTheGivenFilters() throws Exception {
        AddressBarcodeManager manager = manager();
        http.respondToAll(200, json("{'status':'ok','next_page_cursor':'CUR-2','data':[{'route_id':'ROUTE-A1',"
                + "'route_destination_id':5002,'barcode':'BC-1','scan_type':'delivery','lat':40.5,'lng':-74.5}]}"));

        GetAddressBarcodesResponse page = manager.getAddressBarcodes("ROUTE-A1", 5002L, 50, "CUR-1");
        manager.getAddressBarcodes("ROUTE-A1", null, null, null);

        http.request(0).assertEndpoint("GET", HOST, BARCODES_EP).assertApiKeyAsBearer()
                .assertQuery("route_id", "ROUTE-A1")
                .assertQuery("route_destination_id", "5002")
                .assertQuery("limit", "50")
                .assertQuery("cursor", "CUR-1")
                .assertNoBody();
        http.request(1).assertQuery("route_id", "ROUTE-A1").assertNoQuery("route_destination_id")
                .assertNoQuery("limit").assertNoQuery("cursor");
        assertEquals("CUR-2", page.getNextPageCursor());
        assertEquals("BC-1", page.getData().get(0).getBarcode());
        assertEquals(-74.5, page.getData().get(0).getLongitude(), 0.0);
    }

    @Test
    public void saveAddressBarcodesPostsTheScans() throws Exception {
        AddressBarcodeManager manager = manager();
        http.respond(200, json("{'status':true}"));
        BarcodeDataRequest scan = new BarcodeDataRequest();
        scan.setBarcode("BC-1");
        scan.setScanType("delivery");
        scan.setLatitude(40.5);
        scan.setLongitude(-74.5);
        scan.setTimestampUtc(1761040000L);
        scan.setScannedAt("2026-10-21 10:00:00");
        SaveAddressBarcodesRequest request = new SaveAddressBarcodesRequest();
        request.setRouteId("ROUTE-A1");
        request.setRouteDestinationId(5002L);
        request.setBarcodes(Collections.singletonList(scan));

        StatusResponse status = manager.saveAddressBarcodes(request);

        http.request().assertEndpoint("POST", HOST, BARCODES_EP)
                .assertJson("route_id", "ROUTE-A1")
                .assertJson("route_destination_id", 5002)
                .assertJson("barcodes[0].barcode", "BC-1")
                .assertJson("barcodes[0].scan_type", "delivery")
                .assertJson("barcodes[0].lat", 40.5)
                .assertJson("barcodes[0].lng", -74.5)
                .assertJson("barcodes[0].timestamp_utc", 1761040000L)
                .assertJson("barcodes[0].scanned_at", "2026-10-21 10:00:00")
                .assertJsonLacks("barcodes[0].lan", "barcodes[0].timestamp_date");
        assertTrue(status.getStatus());
    }

    /** Regression: the longitude was sent as {@code lan}; the spec and the API name it {@code lng}. */
    @Test
    public void saveAddressBarcodesSendsTheLongitudeAsLng() throws Exception {
        AddressBarcodeManager manager = manager();
        http.respond(200, json("{'status':true}"));
        SaveAddressBarcodesRequest request = request("BC-1", "DELIVERY");
        request.getBarcodes().get(0).setLongitude(-74.5);

        manager.saveAddressBarcodes(request);

        http.request().assertJson("barcodes[0].lng", -74.5).assertJsonLacks("barcodes[0].lan", "barcodes[0].lat");
    }

    @Test
    public void barcodeLongitudeIsReadFromLngOrLan() throws Exception {
        AddressBarcodeManager manager = manager();
        http.respond(200, json("{'data':[{'barcode':'BC-1','lng':-74.5},{'barcode':'BC-2','lan':-75.5}]}"));

        GetAddressBarcodesResponse page = manager.getAddressBarcodes("ROUTE-A1", null, null, null);

        assertEquals(-74.5, page.getData().get(0).getLongitude(), 0.0);
        assertEquals(-75.5, page.getData().get(1).getLongitude(), 0.0);
    }

    @Test
    public void requiredFieldsAreCheckedBeforeSending() {
        final AddressBarcodeManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.getAddressBarcodes(null, null, null, null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.getAddressBarcodes("", 5002L, null, null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.saveAddressBarcodes(null));
        final SaveAddressBarcodesRequest noRoute = request("BC-1", "DELIVERY");
        noRoute.setRouteId(null);
        http.expectThrows(IllegalArgumentException.class, () -> manager.saveAddressBarcodes(noRoute));
        final SaveAddressBarcodesRequest noDestination = request("BC-1", "DELIVERY");
        noDestination.setRouteDestinationId(null);
        http.expectThrows(IllegalArgumentException.class, () -> manager.saveAddressBarcodes(noDestination));
        final SaveAddressBarcodesRequest noBarcodes = request("BC-1", "DELIVERY");
        noBarcodes.setBarcodes(Collections.<BarcodeDataRequest>emptyList());
        http.expectThrows(IllegalArgumentException.class, () -> manager.saveAddressBarcodes(noBarcodes));
        http.expectThrows(IllegalArgumentException.class, () -> manager.saveAddressBarcodes(request(null, "DELIVERY")));
        http.expectThrows(IllegalArgumentException.class, () -> manager.saveAddressBarcodes(request("BC-1", "")));
        http.assertNoRequests();
    }

    /** Builds a save request for one barcode, with fake data. */
    private static SaveAddressBarcodesRequest request(String barcode, String scanType) {
        BarcodeDataRequest scan = new BarcodeDataRequest();
        scan.setBarcode(barcode);
        scan.setScanType(scanType);
        SaveAddressBarcodesRequest request = new SaveAddressBarcodesRequest();
        request.setRouteId("ROUTE-A1");
        request.setRouteDestinationId(5002L);
        request.setBarcodes(new ArrayList<BarcodeDataRequest>(Collections.singletonList(scan)));
        return request;
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        AddressBarcodeManager manager = http.install(new AddressBarcodeManager(MockHttp.API_KEY, true));
        http.respond(200, json("{'data':[]}"));

        manager.getAddressBarcodes("ROUTE-A1", null, null, null);

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final AddressBarcodeManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getAddressBarcodes("ROUTE-A1", null, null, null));
    }
}
