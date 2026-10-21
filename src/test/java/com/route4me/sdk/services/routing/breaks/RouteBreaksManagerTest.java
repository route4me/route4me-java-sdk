package com.route4me.sdk.services.routing.breaks;

import com.route4me.sdk.testsupport.CapturedRequest;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link RouteBreaksManager}: inserting breaks into routes on the V5
 * {@code route-breaks} endpoint.
 */
public class RouteBreaksManagerTest {

    private static final String HOST = "wh.route4me.com";
    private static final String BREAKS_EP = "/modules/api/v5.0/route-breaks";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private RouteBreaksManager manager() {
        return http.install(new RouteBreaksManager(MockHttp.API_KEY));
    }

    @Test
    public void insertBreakAfterTravelTimePostsTheBreakForEveryRoute() throws Exception {
        RouteBreaksManager manager = manager();
        http.respond(200, json("{'status':true}"));

        Object result = manager.insertBreakAfterCertainTravelTime(900, 7200, true, "ROUTE-A1", "ROUTE-A2");

        http.request().assertEndpoint("POST", HOST, BREAKS_EP).assertApiKeyAsBearer()
                .assertContentType("application/json")
                .assertJson("route_id[0]", "ROUTE-A1")
                .assertJson("route_id[1]", "ROUTE-A2")
                .assertJson("replace_existing_breaks", true)
                .assertJson("breaks[0].type", "certain_number_of_travel_time")
                .assertJson("breaks[0].duration", 900)
                .assertJson("breaks[0].params[0]", 7200)
                .assertJson("breaks[0].params[1]", 7200);
        assertEquals(Boolean.TRUE, ((Map<?, ?>) result).get("status"));
    }

    @Test
    public void eachVariantSendsItsBreakType() throws Exception {
        RouteBreaksManager manager = manager();
        http.respondToAll(200, json("{'status':true}"));

        manager.insertBreakAfterCertainLocations(600, 5, false, "ROUTE-A1");
        manager.insertBreakAfterCertainServiceTime(600, 1800, false, "ROUTE-A1");
        manager.insertBreakAfterCertainElapsed(600, 14400, false, "ROUTE-A1");

        assertType(http.request(0), "certain_number_of_locations", 5);
        assertType(http.request(1), "certain_number_of_service_time", 1800);
        assertType(http.request(2), "certain_number_of_total_elapsed_time", 14400);
        http.request(0).assertJson("replace_existing_breaks", false);
    }

    /** Asserts the break type and amount of a captured route-breaks request. */
    private static void assertType(CapturedRequest request, String type, int amount) {
        request.assertEndpoint("POST", HOST, BREAKS_EP)
                .assertJson("breaks[0].type", type)
                .assertJson("breaks[0].params[0]", amount);
    }

    @Test
    public void errorBecomesApiException() {
        final RouteBreaksManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.insertBreakAfterCertainLocations(600, 5, false, "ROUTE-A1"));
    }

    @Ignore("bug #35: a null break amount is auto-unboxed and throws NullPointerException")
    @Test
    public void nullAmountDoesNotThrowNullPointerException() throws Exception {
        RouteBreaksManager manager = manager();
        http.respond(200, json("{'status':true}"));

        try {
            manager.insertBreakAfterCertainTravelTime(900, null, true, "ROUTE-A1");
        } catch (IllegalArgumentException rejected) {
            assertTrue("a clear validation error is fine", rejected.getMessage() != null);
            return;
        } catch (NullPointerException npe) {
            fail("null amount must be rejected clearly or handled, not cause an NPE");
        }
        http.request().assertPath(BREAKS_EP);
    }
}
