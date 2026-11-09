package com.route4me.sdk.services.routing.dynamic.insert;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DynamicInsertManager}: looking up the best route for a new
 * destination on the V5 {@code routes/lookup-for-new-destination} endpoint.
 */
public class DynamicInsertManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String LOOKUP_EP = "/modules/api/v5.0/routes/lookup-for-new-destination";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private DynamicInsertManager manager() {
        return http.install(new DynamicInsertManager(MockHttp.API_KEY));
    }

    @Test
    public void lookForNewDestinationPostsTheLookup() throws Exception {
        DynamicInsertManager manager = manager();
        http.respondWithFixture(200, "matched_routes.json");

        List<MatchedRoute> matches = manager.lookForNewDestination("2026-10-22", 40.5, -74.5,
                InsertMode.OPTIMAL_ANYWHERE, RecommendedBy.DISTANCE, 3, Arrays.asList("ROUTE-A1", "ROUTE-A2"));

        http.request().assertEndpoint("POST", HOST, LOOKUP_EP).assertApiKeyAsBearer()
                .assertContentType("application/json")
                .assertJson("route_info_server", "v5")
                .assertJson("scheduled_for", "2026-10-22")
                .assertJson("lat", 40.5)
                .assertJson("lng", -74.5)
                .assertJson("insert_mode", "optimal_anywhere")
                .assertJson("recommend_by", "distance")
                .assertJson("lookup_results_limit", 3)
                .assertJson("route_ids[1]", "ROUTE-A2");
        assertEquals(1, matches.size());
        assertEquals("ROUTE-A1", matches.get(0).getRouteID());
        assertEquals(Long.valueOf(3), matches.get(0).getRecommendedInsertionStopNumber());
        assertEquals(13.1, matches.get(0).getNewDistance(), 0.0);
    }

    @Test
    public void overloadsLeaveOutRoutesOrDate() throws Exception {
        DynamicInsertManager manager = manager();
        http.respondToAll(200, "[]");

        manager.lookForNewDestination("2026-10-22", 40.5, -74.5, InsertMode.END_OF_ROUTE, RecommendedBy.DURATION, 1);
        manager.lookForNewDestination(40.5, -74.5, InsertMode.BEGINNING_OF_ROUTE, RecommendedBy.DURATION, 1,
                Arrays.asList("ROUTE-A1"));

        http.request(0).assertJson("insert_mode", "end_of_route").assertJson("recommend_by", "duration")
                .assertJsonLacks("route_ids");
        http.request(1).assertJson("insert_mode", "beginning_of_route").assertJsonLacks("scheduled_for")
                .assertJson("route_ids[0]", "ROUTE-A1");
    }

    @Test
    public void errorBecomesApiException() {
        final DynamicInsertManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.lookForNewDestination("2026-10-22", 40.5, -74.5,
                InsertMode.OPTIMAL_AFTER_LAST_VISITED, RecommendedBy.DISTANCE, 1));
    }

    @Test
    public void latitudeAndLongitudeAreRequired() {
        final DynamicInsertManager manager = manager();

        http.expectThrows(IllegalArgumentException.class,
                () -> manager.lookForNewDestination("2026-10-22", null, -74.5, null, null, 3));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.lookForNewDestination(40.5, null, null, null, 3, null));
        http.assertNoRequests();
    }

    @Test
    public void optionalEnumsMayBeNull() throws Exception {
        DynamicInsertManager manager = manager();
        http.respond(200, "[]");

        manager.lookForNewDestination("2026-10-22", 40.5, -74.5, null, null, 3);

        http.request().assertPath(LOOKUP_EP).assertJsonLacks("insert_mode", "recommend_by");
    }
}
