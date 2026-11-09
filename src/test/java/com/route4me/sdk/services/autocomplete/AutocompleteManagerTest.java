package com.route4me.sdk.services.autocomplete;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link AutocompleteManager}: {@code GET /autocomplete.php}.
 */
public class AutocompleteManagerTest {

    private static final String HOST = "api.route4me.com";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private AutocompleteManager manager() {
        return http.install(new AutocompleteManager(MockHttp.API_KEY));
    }

    @Test
    public void autocompleteAddressSendsTheAddress() throws Exception {
        AutocompleteManager manager = manager();
        String reply = json("{'status':'OK','results':[{'address':'100 Example St, Springfield'}]}");
        http.respond(200, reply).respond(200, "No address specified", "text/plain");

        String suggestions = manager.autocompleteAddress("100 Exa");
        String rejected = manager.autocompleteAddress(null);

        http.request(0).assertEndpoint("GET", HOST, "/autocomplete.php").assertApiKeyInQuery()
                .assertQuery("address", "100 Exa")
                .assertNoBody();
        http.request(1).assertNoQuery("address");
        assertEquals(reply, suggestions);
        assertEquals("No address specified", rejected);
    }

    @Test
    public void errorBecomesApiException() {
        final AutocompleteManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.autocompleteAddress("100 Exa"));
    }
}
