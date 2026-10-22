package com.route4me.sdk.services.tracking;

import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link TrackingManager}: sending a GPS position and reading the last
 * known location of a route.
 */
public class TrackingManagerTest {

    private static final String HOST = "api.route4me.com";
    private static final String ROUTE_ID = "C0FFEE0000000000000000000000000A";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private TrackingManager manager() {
        return http.install(new TrackingManager(MockHttp.API_KEY));
    }

    /**
     * Issue #39 reported that setGPS sends the position as a JSON body on a GET. It does not:
     * the position goes into the query string and the request has no body.
     */
    @Test
    public void setGpsSendsThePositionAsQueryParametersWithoutABody() throws Exception {
        TrackingManager manager = manager();
        http.respond(200, json("{'status':true,'tx_id':'TX-1'}"));
        GPSPosition position = new GPSPosition().setRouteId("ROUTE-A1").setMemberId("1001")
                .setLatitude(40.5).setLongitude(-74.5).setAltitude(10.0).setCourse(90).setSpeed(35.5f)
                .setDeviceType(Constants.DeviceType.ANDROID_PHONE).setFormat(Constants.Format.SERIALIZED)
                .setDeviceGUID("DEVICE-1").setDeviceTimestamp("2026-10-21 10:00:00").setAppVersion("1.0");

        SetGpsResponse response = manager.setGPS(position);

        http.request().assertEndpoint("GET", HOST, "/track/set.php").assertApiKeyInQuery()
                .assertQuery("route_id", "ROUTE-A1")
                .assertQuery("member_id", "1001")
                .assertQuery("lat", "40.5")
                .assertQuery("lng", "-74.5")
                .assertQuery("altitude", "10.0")
                .assertQuery("course", "90")
                .assertQuery("speed", "35.5")
                .assertQuery("device_type", "android_phone")
                .assertQuery("format", "serialized")
                .assertQuery("device_guid", "DEVICE-1")
                .assertQuery("device_timestamp", "2026-10-21 10:00:00")
                .assertQuery("app_version", "1.0")
                .assertNoBody();
        assertNull("a GET without a body has no Content-Type", http.request().header("Content-Type"));
        assertEquals(Boolean.TRUE, response.getStatus());
        assertEquals("TX-1", response.getTxId());
    }

    @Test
    public void getLastLocationAsksForTheTrackingHistory() throws Exception {
        TrackingManager manager = manager();
        http.respond(200, json("{'optimization_problem_id':'OPT-1','tracking_history':"
                + "[{'s':'35','lt':40.5,'lg':-74.5,'d':'90','ts':'1761040000','ts_friendly':'2026-10-21 10:00'}]}"));

        DataObject route = manager.getLastLocation("ROUTE-A1");

        http.request().assertEndpoint("GET", HOST, "/api.v4/route.php").assertQuery("route_id", "ROUTE-A1")
                .assertQuery("device_tracking_history", "1");
        TrackingHistory point = route.getTrackingHistory().get(0);
        assertEquals(40.5, point.getLatitude(), 0.0);
        assertEquals(-74.5, point.getLongitude(), 0.0);
        assertEquals("1761040000", point.getTimestamp());
        assertEquals("2026-10-21 10:00", point.getFriendlyTimestamp());
    }

    @Test
    public void getDeviceLocationHistorySendsThePeriodAsJsonQuery() throws Exception {
        TrackingManager manager = manager();
        http.respondToAll(200, json("{'data':[{'s':'35','su':'mph','m':'1001','lt':40.5,'lg':-74.5,'d':'90',"
                + "'ts':'1761040000','ts_friendly':'2026-10-21 10:00','src':'R4M'}],"
                + "'mmd':{'summary':{'distance':12}}}"));
        DeviceLocationHistoryRequest request = new DeviceLocationHistoryRequest().setRouteId(ROUTE_ID)
                .setMemberId(1001L).setTimePeriod("custom").setStartDate(1761000000L).setEndDate(1761090000L)
                .setLastPosition(false);

        DeviceLocationHistoryResponse history = manager.getDeviceLocationHistory(request);
        manager.getDeviceLocationHistory(new DeviceLocationHistoryRequest().setRouteId(ROUTE_ID)
                .setTimePeriod("today"));

        http.request(0).assertEndpoint("GET", HOST, "/api/track/get_device_location.php").assertApiKeyInQuery()
                .assertQuery("format", "json")
                .assertQuery("route_id", ROUTE_ID)
                .assertQuery("member_id", "1001")
                .assertQuery("time_period", "custom")
                .assertQuery("start_date", "1761000000")
                .assertQuery("end_date", "1761090000")
                .assertQuery("last_position", "0")
                .assertNoBody();
        http.request(1).assertQuery("time_period", "today").assertNoQuery("member_id").assertNoQuery("start_date")
                .assertNoQuery("last_position");
        TrackingHistory point = history.getData().get(0);
        assertEquals(40.5, point.getLatitude(), 0.0);
        assertEquals("mph", point.getSpeedUnit());
        assertEquals("1001", point.getMemberId());
        assertEquals("R4M", point.getSource());
        assertTrue(history.getMmd().containsKey("summary"));
    }

    @Test
    public void getUserLocationsSendsTheQueryOnlyWhenGiven() throws Exception {
        TrackingManager manager = manager();
        http.respondToAll(200, json("[{'member_data':{'member_id':1001,'member_email':'driver@example.com'},"
                + "'tracking':{'position_lat':40.5,'position_lng':-74.5,'route_id':'" + ROUTE_ID + "',"
                + "'activity_timestamp':1761040000,'speed':12.5,'member_id':1001},'from_cache':true}]"));

        UserLocation[] locations = manager.getUserLocations(null);
        manager.getUserLocations("driver@example.com");

        http.request(0).assertEndpoint("GET", HOST, "/api/track/view_user_locations.php").assertApiKeyInQuery()
                .assertNoQuery("query").assertNoBody();
        http.request(1).assertQuery("query", "driver@example.com");
        assertEquals("driver@example.com", locations[0].getMemberData().get("member_email"));
        assertEquals(40.5, locations[0].getTracking().getPositionLat(), 0.0);
        assertEquals(ROUTE_ID, locations[0].getTracking().getRouteId());
        assertEquals(Long.valueOf(1761040000L), locations[0].getTracking().getActivityTimestamp());
        assertTrue(locations[0].getFromCache());
    }

    @Test
    public void setTrackingPingsPostsOnePing() throws Exception {
        TrackingManager manager = manager();
        http.respond(200, json("{'status':true,'tx_id':'TX-1','activity_at':1761040000,'is_mobile':true,"
                + "'mph_speed':28.9}"));
        TrackingPingsRequest request = new TrackingPingsRequest();
        request.setMemberId(1001L);
        request.setDeviceGuid("DEVICE-1");
        request.setRouteId(ROUTE_ID);
        request.setLat(40.5);
        request.setLng(-74.5);
        request.setSpeed(12.9);
        request.setSpeedUnit("m/s");
        request.setDeviceTimestamp(1761040000L);
        request.setExtendResponseData(true);

        TrackingPingsResponse response = manager.setTrackingPings(request);

        http.request().assertEndpoint("POST", HOST, "/track/set.php").assertApiKeyInQuery()
                .assertContentType("application/json")
                .assertJson("member_id", 1001)
                .assertJson("device_guid", "DEVICE-1")
                .assertJson("route_id", ROUTE_ID)
                .assertJson("lat", 40.5)
                .assertJson("lng", -74.5)
                .assertJson("speed_unit", "m/s")
                .assertJson("device_timestamp", 1761040000)
                .assertJson("extend_response_data", true)
                .assertJsonLacks("items", "vehicle_id");
        assertTrue(response.getStatus());
        assertEquals("TX-1", response.getTxId());
        assertEquals(Long.valueOf(1761040000L), response.getActivityAt());
        assertTrue(response.getIsMobile());
        assertEquals(28.9, response.getMphSpeed(), 0.0);
    }

    @Test
    public void setTrackingPingsPostsABatch() throws Exception {
        TrackingManager manager = manager();
        http.respond(200, json("{'status':true,'items_errors':[{'index':1,'message':'Some of the coordinates are set "
                + "to 0.0'}]}"));
        TrackingPing first = new TrackingPing();
        first.setLat(40.5);
        first.setLng(-74.5);
        first.setDeviceTimestamp("2026-10-21 10:00:00");
        TrackingPing second = new TrackingPing();
        second.setLat(40.6);
        second.setLng(-74.6);
        second.setCustomData(Collections.<String, Object>singletonMap("fuel_level", 87));
        TrackingPingsRequest request = new TrackingPingsRequest();
        request.setMemberId(1001L);
        request.setDeviceType("iphone");
        request.setVehicleId("C0FFEE00000000000000000000000001");
        request.setItems(Arrays.asList(first, second));

        TrackingPingsResponse response = manager.setTrackingPings(request);

        http.request().assertEndpoint("POST", HOST, "/track/set.php")
                .assertJson("member_id", 1001)
                .assertJson("device_type", "iphone")
                .assertJson("vehicle_id", "C0FFEE00000000000000000000000001")
                .assertJson("items[0].lat", 40.5)
                .assertJson("items[0].device_timestamp", "2026-10-21 10:00:00")
                .assertJson("items[1].custom_data.fuel_level", 87)
                .assertJsonLacks("lat", "lng");
        assertEquals(Integer.valueOf(1), response.getItemsErrors().get(0).getIndex());
        assertEquals("Some of the coordinates are set to 0.0", response.getItemsErrors().get(0).getMessage());
    }

    @Test
    public void setTrackingPingsChecksTheRequiredFieldsBeforeSending() {
        final TrackingManager manager = manager();
        final TrackingPingsRequest noCoordinates = new TrackingPingsRequest();
        noCoordinates.setLat(40.5);
        final TrackingPingsRequest emptyBatch = new TrackingPingsRequest();
        emptyBatch.setItems(Collections.<TrackingPing>emptyList());
        final TrackingPingsRequest bigBatch = new TrackingPingsRequest();
        List<TrackingPing> pings = new ArrayList<>();
        for (int i = 0; i <= TrackingPingsRequest.MAX_ITEMS; i++) {
            TrackingPing ping = new TrackingPing();
            ping.setLat(40.5);
            ping.setLng(-74.5);
            pings.add(ping);
        }
        bigBatch.setItems(pings);
        final TrackingPingsRequest badItem = new TrackingPingsRequest();
        TrackingPing noLng = new TrackingPing();
        noLng.setLat(40.5);
        badItem.setItems(Arrays.asList(pings.get(0), noLng));

        http.expectThrows(IllegalArgumentException.class, () -> manager.setTrackingPings(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.setTrackingPings(noCoordinates));
        http.expectThrows(IllegalArgumentException.class, () -> manager.setTrackingPings(emptyBatch));
        http.expectThrows(IllegalArgumentException.class, () -> manager.setTrackingPings(bigBatch));
        IllegalArgumentException itemError = http.expectThrows(IllegalArgumentException.class,
                () -> manager.setTrackingPings(badItem));

        http.assertNoRequests();
        assertEquals("Item 1 needs lat and lng", itemError.getMessage());
    }

    @Test
    public void verifyDeviceLicenseSendsTheDeviceInTheQueryAndBody() throws Exception {
        TrackingManager manager = manager();
        http.respond(200, "Missing or Invalid Device ID", "text/plain").respond(200, json("{'status':true}"));

        String rejected = manager.verifyDeviceLicense("DEVICE-1", "android_phone", "json");
        manager.verifyDeviceLicense(null, null, "json");

        http.request(0).assertEndpoint("POST", HOST, "/api/device/verify_device_license.php").assertApiKeyInQuery()
                .assertQuery("device_id", "DEVICE-1")
                .assertQuery("device_type", "android_phone")
                .assertJson("device_id", "DEVICE-1")
                .assertJson("device_type", "android_phone")
                .assertJson("format", "json");
        http.request(1).assertNoQuery("device_id").assertNoQuery("device_type").assertJsonLacks("device_id")
                .assertJson("format", "json");
        assertEquals("Missing or Invalid Device ID", rejected);
    }

    @Test
    public void errorBecomesApiException() {
        final TrackingManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getLastLocation("ROUTE-A1"));
    }
}
