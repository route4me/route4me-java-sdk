package com.route4me.sdk.services.addressbarcode;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

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
                + "'route_destination_id':5002,'barcode':'BC-1','scan_type':'delivery','lat':40.5,'lan':-74.5}]}"));

        GetAddressBarcodesResponse page = manager.getAddressBarcodes("ROUTE-A1", 5002L, 50, "CUR-1");
        manager.getAddressBarcodes(null, null, null, null);

        http.request(0).assertEndpoint("GET", HOST, BARCODES_EP).assertApiKeyInQuery()
                .assertQuery("route_id", "ROUTE-A1")
                .assertQuery("route_destination_id", "5002")
                .assertQuery("limit", "50")
                .assertQuery("cursor", "CUR-1")
                .assertNoBody();
        http.request(1).assertNoQuery("route_id").assertNoQuery("route_destination_id").assertNoQuery("limit")
                .assertNoQuery("cursor");
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
                .assertJson("barcodes[0].lan", -74.5)
                .assertJson("barcodes[0].timestamp_utc", 1761040000L)
                .assertJson("barcodes[0].scanned_at", "2026-10-21 10:00:00");
        assertTrue(status.getStatus());
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
