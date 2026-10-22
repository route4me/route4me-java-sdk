package com.route4me.sdk.services.routecustomdata;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link RouteCustomDataManager}: custom key-value data on routes, on the
 * V5 API.
 */
public class RouteCustomDataManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String ROUTES_EP = "/modules/api/v5.0/routes";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private RouteCustomDataManager manager() {
        return http.install(new RouteCustomDataManager(MockHttp.API_KEY));
    }

    @Test
    public void getAndUpdateRouteCustomData() throws Exception {
        RouteCustomDataManager manager = manager();
        http.respondToAll(200, json("{'region':'north','shift':'am'}"));

        Map<String, String> data = manager.getRouteCustomData("ROUTE-A1");
        Map<String, String> updated = manager.updateRouteCustomData("ROUTE-A1",
                Collections.singletonMap("region", "north"));

        http.request(0).assertEndpoint("GET", HOST, ROUTES_EP + "/ROUTE-A1/custom-data").assertApiKeyAsBearer()
                .assertNoBody();
        http.request(1).assertEndpoint("PUT", HOST, ROUTES_EP + "/ROUTE-A1/custom-data").assertJson("region", "north");
        assertEquals("am", data.get("shift"));
        assertEquals("north", updated.get("region"));
    }

    @Test
    public void getBulkRouteCustomDataPostsTheRouteIds() throws Exception {
        RouteCustomDataManager manager = manager();
        http.respond(200, json("{'data':{'ROUTE-A1':{'region':'north'},'ROUTE-A2':{'region':'south'}}}"));

        RouteCustomDataCollection collection = manager.getBulkRouteCustomData(new String[] {"ROUTE-A1", "ROUTE-A2"});

        http.request().assertEndpoint("POST", HOST, ROUTES_EP + "/custom-data/bulk")
                .assertJson("route_ids[0]", "ROUTE-A1").assertJson("route_ids[1]", "ROUTE-A2");
        assertEquals("south", collection.getData().get("ROUTE-A2").get("region"));
    }

    @Test
    public void getCustomDataKeysListsTheKeys() throws Exception {
        RouteCustomDataManager manager = manager();
        http.respond(200, json("['region','shift']"));

        String[] keys = manager.getCustomDataKeys();

        http.request().assertEndpoint("GET", HOST, ROUTES_EP + "/custom-data/keys");
        assertArrayEquals(new String[] {"region", "shift"}, keys);
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        RouteCustomDataManager manager = http.install(new RouteCustomDataManager(MockHttp.API_KEY, true));
        http.respond(200, "[]");

        manager.getCustomDataKeys();

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final RouteCustomDataManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getRouteCustomData("ROUTE-A1"));
    }
}
