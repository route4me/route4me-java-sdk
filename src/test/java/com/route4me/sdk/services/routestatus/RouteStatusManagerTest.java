package com.route4me.sdk.services.routestatus;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link RouteStatusManager}: route and stop statuses on the V5 API.
 */
public class RouteStatusManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String STATUS_EP = "/modules/api/v5.0/route-status";
    private static final String ROUTE_STATUS =
            json("{'route_id':'ROUTE-A1','status':'started','lat':40.5,'lng':-74.5,'started_at':1761040000}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private RouteStatusManager manager() {
        return http.install(new RouteStatusManager(MockHttp.API_KEY));
    }

    @Test
    public void getRouteStatusReadsTheStatus() throws Exception {
        RouteStatusManager manager = manager();
        http.respond(200, ROUTE_STATUS);

        RouteStatusResponse status = manager.getRouteStatus("ROUTE-A1");

        http.request().assertEndpoint("GET", HOST, STATUS_EP + "/ROUTE-A1").assertApiKeyAsBearer().assertNoBody();
        assertEquals("started", status.getStatus());
        assertEquals(Long.valueOf(1761040000L), status.getStartedAt());
        assertEquals(40.5, status.getLatitude(), 0.0);
    }

    @Test
    public void updateRouteStatusPostsTheNewStatus() throws Exception {
        RouteStatusManager manager = manager();
        http.respond(200, ROUTE_STATUS);
        RouteStatusRequest request = new RouteStatusRequest();
        request.setStatus("started");
        request.setLatitude(40.5);
        request.setLongitude(-74.5);
        request.setEventTimestamp(1761040000L);

        manager.updateRouteStatus("ROUTE-A1", request);

        http.request().assertEndpoint("POST", HOST, STATUS_EP + "/ROUTE-A1")
                .assertJson("status", "started")
                .assertJson("lat", 40.5)
                .assertJson("lng", -74.5)
                .assertJson("event_timestamp", 1761040000L)
                .assertNoQuery("device_id");
    }

    @Test
    public void updateRouteStatusSendsTheDeviceAndLeavesOutAnUnknownLocation() throws Exception {
        RouteStatusManager manager = manager();
        http.respond(200, ROUTE_STATUS);
        RouteStatusRequest request = new RouteStatusRequest();
        request.setStatus("paused");
        request.setReason("Traffic jam");
        request.setReasonCode("TRAFFIC");

        manager.updateRouteStatus("ROUTE-A1", request, "device-123");

        http.request().assertEndpoint("POST", HOST, STATUS_EP + "/ROUTE-A1")
                .assertQuery("device_id", "device-123")
                .assertJson("status", "paused")
                .assertJson("reason", "Traffic jam")
                .assertJson("reason_code", "TRAFFIC")
                .assertJsonLacks("lat", "lng", "event_timestamp");
    }

    @Test
    public void requiredValuesAreCheckedBeforeSending() {
        final RouteStatusManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.updateRouteStatusAsPlanned(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateRouteStatusAsPlanned(new String[0]));
        http.expectThrows(IllegalArgumentException.class, () -> manager.setRouteStopStatus(null, "completed"));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.setRouteStopStatus(new long[0], "completed"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.setRouteStopStatus(new long[] {5002}, null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.setRouteStopStatus(new long[] {5002}, ""));
        http.assertNoRequests();
    }

    @Test
    public void emptyRouteIdsAreRejectedBeforeSending() {
        final RouteStatusManager manager = manager();

        for (final String id : Arrays.asList(null, "")) {
            http.expectThrows(IllegalArgumentException.class, () -> manager.getRouteStatus(id));
            http.expectThrows(IllegalArgumentException.class,
                    () -> manager.updateRouteStatus(id, new RouteStatusRequest()));
            http.expectThrows(IllegalArgumentException.class,
                    () -> manager.getRouteStatusHistory(id, null, null, null));
            http.expectThrows(IllegalArgumentException.class, () -> manager.rollbackRouteStatus(id));
        }
        http.assertNoRequests();
    }

    @Test
    public void routeIdsWithASlashStayInOneSegment() throws Exception {
        RouteStatusManager manager = manager();
        http.respondToAll(200, ROUTE_STATUS);

        manager.updateRouteStatus("../secret", new RouteStatusRequest());

        assertEquals(STATUS_EP + "/..%2Fsecret", http.request().getUri().getRawPath());
    }

    @Test
    public void getRouteStatusHistorySendsOnlyTheGivenFilters() throws Exception {
        RouteStatusManager manager = manager();
        http.respondToAll(200, json("{'data':[{'route_status_history_id':'H-1','route_id':'ROUTE-A1',"
                + "'status':'planned','timestamp':1761040000}]}"));

        RouteStatusHistoryResponse history = manager.getRouteStatusHistory("ROUTE-A1", "desc", 1L, 2L);
        manager.getRouteStatusHistory("ROUTE-A1", null, null, null);

        http.request(0).assertEndpoint("GET", HOST, STATUS_EP + "/ROUTE-A1/history")
                .assertQuery("order_by", "desc").assertQuery("start", "1").assertQuery("end", "2");
        http.request(1).assertNoQuery("order_by").assertNoQuery("start").assertNoQuery("end");
        assertEquals("H-1", history.getData().get(0).getRouteStatusHistoryId());
    }

    @Test
    public void rollbackRouteStatusUsesGet() throws Exception {
        RouteStatusManager manager = manager();
        http.respond(200, ROUTE_STATUS);

        manager.rollbackRouteStatus("ROUTE-A1");

        http.request().assertEndpoint("GET", HOST, STATUS_EP + "/ROUTE-A1/rollback");
    }

    @Test
    public void updateRouteStatusAsPlannedPostsTheRouteIds() throws Exception {
        RouteStatusManager manager = manager();
        http.respond(200, "{\"data\":[" + ROUTE_STATUS + "]}");

        UpdateRouteStatusAsPlannedResponse response = manager.updateRouteStatusAsPlanned(
                new String[] {"ROUTE-A1", "ROUTE-A2"});

        http.request().assertEndpoint("POST", HOST, STATUS_EP + "/planned")
                .assertJson("route_ids[0]", "ROUTE-A1")
                .assertJson("route_ids[1]", "ROUTE-A2");
        assertEquals("ROUTE-A1", response.getData().get(0).getRouteId());
    }

    @Test
    public void setRouteStopStatusPostsTheDestinations() throws Exception {
        RouteStatusManager manager = manager();
        http.respond(200, json("{'status':true}"));

        StatusResponse response = manager.setRouteStopStatus(new long[] {5001L, 5002L}, "completed");

        http.request().assertEndpoint("POST", HOST, "/modules/api/v5.0/route-stop-status")
                .assertJson("destination_ids[1]", 5002)
                .assertJson("status", "completed");
        assertTrue(response.getStatus());
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        RouteStatusManager manager = http.install(new RouteStatusManager(MockHttp.API_KEY, true));
        http.respond(200, ROUTE_STATUS);

        manager.getRouteStatus("ROUTE-A1");

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final RouteStatusManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getRouteStatus("ROUTE-A1"));
    }
}
