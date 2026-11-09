package com.route4me.sdk.services.distance;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DistanceManager}: {@code GET /api/distance.php}.
 */
public class DistanceManagerTest {

    private static final String HOST = "api.route4me.com";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private DistanceManager manager() {
        return http.install(new DistanceManager(MockHttp.API_KEY));
    }

    @Test
    public void calculateDistancePassesTheQueryThrough() throws Exception {
        DistanceManager manager = manager();
        http.respond(200, "12.5", "text/plain").respond(200, "Missing or invalid start and end address.", "text/plain");
        Map<String, String> query = new LinkedHashMap<>();
        query.put("start_address", "100 Example St, Springfield");
        query.put("end_address", "200 Sample Ave, Springfield");

        String distance = manager.calculateDistance(query);
        String rejected = manager.calculateDistance(null);

        http.request(0).assertEndpoint("GET", HOST, "/api/distance.php").assertApiKeyInQuery()
                .assertQuery("start_address", "100 Example St, Springfield")
                .assertQuery("end_address", "200 Sample Ave, Springfield")
                .assertNoBody();
        assertEquals(1, http.request(1).getQueryParameters().size());
        assertEquals("12.5", distance);
        assertEquals("Missing or invalid start and end address.", rejected);
    }

    @Test
    public void anApiKeyInTheQueryIsRejected() {
        final DistanceManager manager = manager();

        http.expectThrows(IllegalArgumentException.class,
                () -> manager.calculateDistance(Collections.singletonMap("API_KEY", "x")));
        http.assertNoRequests();
    }

    @Test
    public void errorBecomesApiException() {
        final DistanceManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.calculateDistance(null));
    }
}
