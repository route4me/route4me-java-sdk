package com.route4me.sdk.services.telematics;

import com.google.gson.Gson;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.testsupport.Fixtures;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link TelematicsManager}: telematics registration, connections and vendors.
 *
 * <p>The deprecated vendor methods of the retired {@code telematics.route4me.com} host throw
 * without sending a request, so the API key never reaches that host.
 */
public class TelematicsManagerTest {

    private static final String API = "api.route4me.com";
    private static final String WH = "wh.route4me.com";
    private static final String CONNECTIONS_EP = "/api.v4/telematics/connections.php";
    /** A made-up telematics API token, not the account API key. */
    private static final String TOKEN = "TELEMATICS-TOKEN";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private TelematicsManager manager() {
        return http.install(new TelematicsManager(MockHttp.API_KEY));
    }

    /** Builds a sample telematics connection with fake vendor credentials. */
    private static TelematicsConnection credentials() {
        TelematicsConnection credentials = new TelematicsConnection();
        credentials.setName("Example connection");
        credentials.setVendor("example-fleet");
        credentials.setVendorID(55);
        credentials.setAccountID("ACCOUNT-1");
        credentials.setUsername("fleet-user");
        credentials.setPassword("vendor-password");
        credentials.setApiKey("VENDOR-KEY");
        credentials.setIsEnabled(1);
        return credentials;
    }

    // ------------------------------------------------------------------ registration and connections

    @Test
    public void registerMemberSendsTheMemberId() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, json("{'api_token':'TELEMATICS-TOKEN','id':'REG-1','created_at':'2026-10-21'}"));

        TelematicsMemberRegister registration = manager.registerMember(1001);

        http.request().assertEndpoint("GET", API, "/api.v4/telematics/register.php").assertQuery("member_id", "1001")
                .assertApiKeyInQuery();
        assertEquals(TOKEN, registration.getApiToken());
        assertEquals("REG-1", registration.getTelematicRegisterID());
    }

    @Test
    public void getTelematicsConnectionsSendsTheToken() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, "[" + Fixtures.read("telematics_connection.json") + "]");

        List<TelematicsConnection> connections = manager.getTelematicsConnections(TOKEN);

        http.request().assertEndpoint("GET", API, CONNECTIONS_EP).assertQuery("api_token", TOKEN);
        assertEquals(1, connections.size());
    }

    @Test
    public void getTelematicsConnectionsReturnsConnections() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, "[" + Fixtures.read("telematics_connection.json") + "]");

        List<TelematicsConnection> connections = manager.getTelematicsConnections(TOKEN);

        Object first = connections.get(0);
        assertTrue("got " + first.getClass().getName(), first instanceof TelematicsConnection);
        assertEquals("CONNECTION-1", connections.get(0).getConnectionToken());
    }

    @Test
    public void createTelematicsConnectionPostsTheCredentialsAsAForm() throws Exception {
        TelematicsManager manager = manager();
        http.respondWithFixture(200, "telematics_connection.json");

        TelematicsConnection created = manager.createTelematicsConnection(TOKEN, credentials());

        http.request().assertEndpoint("POST", API, CONNECTIONS_EP).assertQuery("api_token", TOKEN)
                .assertContentType("application/x-www-form-urlencoded")
                .assertForm("name", "Example connection")
                .assertForm("vendor", "example-fleet")
                .assertForm("vendor_id", "55")
                .assertForm("account_id", "ACCOUNT-1")
                .assertForm("username", "fleet-user")
                .assertForm("is_enabled", "1");
        assertEquals("CONNECTION-1", created.getConnectionToken());
        assertEquals(Integer.valueOf(3), created.getSyncedVehiclesCount());
    }

    @Test
    public void updateDeleteAndGetConnectionSendTheConnectionToken() throws Exception {
        TelematicsManager manager = manager();
        http.respondToAll(200, Fixtures.read("telematics_connection.json"));

        manager.updateTelematicsConnection(TOKEN, "CONNECTION-1", credentials());
        manager.deleteTelematicsConnection(TOKEN, "CONNECTION-1");
        TelematicsConnection fetched = manager.getTelematicsConnection(TOKEN, "CONNECTION-1");

        http.request(0).assertEndpoint("PUT", API, CONNECTIONS_EP).assertQuery("connection_token", "CONNECTION-1")
                .assertQuery("api_token", TOKEN).assertForm("username", "fleet-user");
        http.request(1).assertEndpoint("DELETE", API, CONNECTIONS_EP).assertQuery("connection_token", "CONNECTION-1")
                .assertNoBody();
        http.request(2).assertEndpoint("GET", API, CONNECTIONS_EP).assertQuery("connection_token", "CONNECTION-1");
        assertEquals("Example connection", fetched.getName());
    }

    @Test
    public void getTelematicsVendorSendsTheToken() throws Exception {
        TelematicsManager manager = manager();
        http.respondWithFixture(200, "telematics_vendors.json");

        TelematicsVendor[] vendors = manager.getTelematicsVendor(TOKEN);

        http.request().assertEndpoint("GET", API, "/api.v4/telematics/vendors.php").assertQuery("api_token", TOKEN);
        assertEquals("Example Fleet", vendors[0].getTitle());
        assertEquals("password", vendors[0].getCredentials().get(1).getName());
    }

    @Test
    public void connectionErrorBecomesApiException() {
        final TelematicsManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getTelematicsConnection(TOKEN, "CONNECTION-1"));
    }

    // ------------------------------------------------------------------ vendors (retired host)

    /** Expects a call on the retired host to throw without sending anything. */
    private void assertRetired(MockHttp.Call call) {
        APIException error = http.expectApiException(call);
        assertEquals(TelematicsManager.RETIRED_HOST_MESSAGE, error.getMessage());
        assertTrue(error.getMessage(), error.getMessage().contains("getTelematicsVendorsV5"));
        http.assertNoRequests();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void retiredHostMethodsThrowWithoutSendingTheKey() {
        final TelematicsManager manager = manager();
        http.respondToAll(200, Fixtures.read("telematics_vendors_info.json"));

        assertRetired(manager::getTelematicsVendorsInfo);
        assertRetired(() -> manager.getTelematicsVendorInfo("55"));
        assertRetired(() -> manager.searchVendorByCountry("GB"));
        assertRetired(() -> manager.searchVendorByCountry("GB", "2", "10"));
        assertRetired(() -> manager.searchVendorBySize("global"));
        assertRetired(() -> manager.searchVendorBySize("global", "1", "5"));
        assertRetired(() -> manager.searchVendorByKeyWord("fleet"));
        assertRetired(() -> manager.searchVendorByKeyWord("fleet", "1", "5"));
        assertRetired(() -> manager.searchVendorByFeature("satellite"));
        assertRetired(() -> manager.searchVendorByFeature("satellite", "1", "5"));
        assertRetired(() -> manager.compareTelematicsVendors("55,56"));
    }

    @Test
    public void retiredHostModelsStillParse() {
        TelematicsVendorsInfo info = new Gson().fromJson(
                Fixtures.read("telematics_vendors_info.json"), TelematicsVendorsInfo.class);

        assertEquals(2, info.getVendors().size());
        assertEquals("engine-hours", info.getVendors().get(0).getFeatures().get(1).getSlug());
    }

    // ------------------------------------------------------------------ V5 vendors

    @Test
    public void getTelematicsVendorsV5ListsTheCatalog() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, json("{'data':[{'id':'55','name':'Example Fleet','slug':'example-fleet','is_integrated':true,"
                + "'size':'global','features':[{'id':'1','name':'Satellite'}]}]}"));

        TelematicsVendorsV5Response vendors = manager.getTelematicsVendorsV5();

        http.request().assertEndpoint("GET", WH, "/modules/api/v5.0/telematics/vendors/all").assertApiKeyAsBearer()
                .assertNoBody();
        assertTrue("no filters, no query", http.request().getQueryParameters().isEmpty());
        assertEquals("Example Fleet", vendors.getData().get(0).getName());
        assertEquals(Boolean.TRUE, vendors.getData().get(0).getIsIntegrated());
        assertEquals("Satellite", vendors.getData().get(0).getFeatures().get(0).getName());
    }

    @Test
    public void getTelematicsVendorsV5SendsTheFilters() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, json("{'data':[]}")).respond(200, json("{'data':[]}"));
        TelematicsVendorsV5Request filters = new TelematicsVendorsV5Request().setSearch("fleet").setCountry("US")
                .setFeature("satellite").setFeatures(Arrays.asList("gps", "engine hours")).setSize("global")
                .setIsIntegrated(true).setDisplayHidden(false).setPage(2).setPerPage(50);

        manager.getTelematicsVendorsV5(filters);
        manager.getTelematicsVendorsV5(new TelematicsVendorsV5Request());

        http.request(0).assertQuery("search", "fleet").assertQuery("country", "US").assertQuery("feature", "satellite")
                .assertQueryValues("features[]", "gps", "engine hours").assertQuery("size", "global")
                .assertQuery("is_integrated", "1").assertQuery("display_hidden", "0")
                .assertQuery("page", "2").assertQuery("per_page", "50");
        assertTrue(http.request(1).getQueryParameters().isEmpty());
    }

    @Test
    public void getTelematicsVendorV5GetsOneVendor() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, json("{'id':55,'is_enabled':true,'is_readonly':false,'name':'tomtom','title':'TomTom',"
                + "'icon':null,'hint':'Use the API "
                        + "key','created_at':'2026-01-01T00:00:00Z','updated_at':'2026-02-01T00:00:00Z',"
                + "'sync_entities':[{'id':'vehicles','alias':'vehicles','title':'Vehicles','default_value':true}],"
                + "'credentials':[{'name':'api_key','is_required':true,'default':null}]}"));

        TelematicsVendorV5 vendor = manager.getTelematicsVendorV5(55);

        http.request().assertEndpoint("GET", WH, "/modules/api/v5.0/telematics/vendors").assertQuery("vendor_id", "55")
                .assertApiKeyAsBearer();
        assertEquals(Long.valueOf(55), vendor.getId());
        assertEquals(Boolean.TRUE, vendor.getIsEnabled());
        assertEquals(Boolean.FALSE, vendor.getIsReadonly());
        assertEquals("tomtom", vendor.getName());
        assertEquals("TomTom", vendor.getTitle());
        assertNull(vendor.getIcon());
        assertEquals("Use the API key", vendor.getHint());
        assertEquals("2026-01-01T00:00:00Z", vendor.getCreatedAt());
        assertEquals("2026-02-01T00:00:00Z", vendor.getUpdatedAt());
        assertEquals("vehicles", vendor.getSyncEntities().get(0).getAlias());
        assertEquals("Vehicles", vendor.getSyncEntities().get(0).getTitle());
        assertEquals("vehicles", vendor.getSyncEntities().get(0).getId());
        assertEquals(Boolean.TRUE, vendor.getSyncEntities().get(0).getDefaultValue());
        assertEquals("api_key", vendor.getCredentials().get(0).getName());
        assertEquals(Boolean.TRUE, vendor.getCredentials().get(0).getIsRequired());
        assertNull(vendor.getCredentials().get(0).getDefaultValue());
    }

    @Test
    public void v5VendorErrorBecomesApiException() {
        final TelematicsManager manager = manager();
        http.respond(404, json("{'status':false,'code':404,'messages':{'vendor_id':['Not found']}}"));

        APIException error = http.expectApiException(() -> manager.getTelematicsVendorV5(99));

        assertEquals(404, error.getStatusCode());
    }
}
