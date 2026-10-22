package com.route4me.sdk.services.drivers;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DriversManager}: the drivers of the account on the V4 API.
 */
public class DriversManagerTest {

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private DriversManager manager() {
        return http.install(new DriversManager(MockHttp.API_KEY));
    }

    @Test
    public void getDriversReturnsTheAnswerAsText() throws Exception {
        DriversManager manager = manager();
        String answer = json("[{'member_id':1002,'member_first_name':'Sam'}]");
        http.respond(200, answer);

        String drivers = manager.getDrivers();

        http.request().assertEndpoint("GET", "api.route4me.com", "/api/drivers/view_drivers.php")
                .assertApiKeyInQuery().assertNoBody();
        assertEquals(answer, drivers);
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        DriversManager manager = http.install(new DriversManager(MockHttp.API_KEY, true));
        http.respond(200, "[]");

        manager.getDrivers();

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final DriversManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getDrivers());
    }
}
