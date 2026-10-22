package com.route4me.sdk.services.tracking;

import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link TrackingManager}: sending a GPS position and reading the last
 * known location of a route.
 */
public class TrackingManagerTest {

    private static final String HOST = "api.route4me.com";

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
    public void errorBecomesApiException() {
        final TrackingManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getLastLocation("ROUTE-A1"));
    }
}
