package com.route4me.sdk.services.authhistory;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link AuthHistoryManager}: the authentication history on the V5 API.
 */
public class AuthHistoryManagerTest {

    private static final String HOST = "wh.route4me.com";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private AuthHistoryManager manager() {
        return http.install(new AuthHistoryManager(MockHttp.API_KEY));
    }

    @Test
    public void getApiConfigReadsTheCapabilitiesAndPreferences() throws Exception {
        AuthHistoryManager manager = manager();
        http.respond(200, json("{'api_capabilities':{'sortable_fields':['created_at'],"
                + "'filterable_fields':['member_id','device_type'],'search':true},"
                + "'api_preferences':{'force_server_side_search':true,'partial_load':false,"
                + "'simple_pagination':true}}"));

        AuthHistoryApiConfig config = manager.getApiConfig();

        http.request().assertEndpoint("GET", HOST, "/modules/api/v5.0/auth-history/apiconf").assertApiKeyAsBearer()
                .assertNoBody();
        assertEquals("created_at", config.getApiCapabilities().getSortableFields().get(0));
        assertEquals("device_type", config.getApiCapabilities().getFilterableFields().get(1));
        assertTrue(config.getApiCapabilities().getSearch());
        assertTrue(config.getApiPreferences().getForceServerSideSearch());
        assertFalse(config.getApiPreferences().getPartialLoad());
        assertTrue(config.getApiPreferences().getSimplePagination());
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        AuthHistoryManager manager = http.install(new AuthHistoryManager(MockHttp.API_KEY, true));
        http.respond(200, "{}");

        manager.getApiConfig();

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final AuthHistoryManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getApiConfig());
    }
}
