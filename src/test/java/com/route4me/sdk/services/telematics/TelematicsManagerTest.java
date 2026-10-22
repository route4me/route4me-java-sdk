package com.route4me.sdk.services.telematics;

import com.route4me.sdk.testsupport.Fixtures;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link TelematicsManager}: telematics registration, connections and vendors.
 *
 * <p>The vendor methods still call {@code telematics.route4me.com}, which is retired. The
 * endpoint registry lists it as deprecated, so these tests pass, and the guard reports
 * each hit in the deprecated-endpoint summary.
 */
public class TelematicsManagerTest {

    private static final String API = "api.route4me.com";
    private static final String LEGACY_HOST = "telematics.route4me.com";
    private static final String VENDORS_EP = "/api/vendors.php";
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

    @Test
    public void getTelematicsVendorsInfoUsesTheRetiredHost() throws Exception {
        TelematicsManager manager = manager();
        http.respondWithFixture(200, "telematics_vendors_info.json");

        TelematicsVendorsInfo info = manager.getTelematicsVendorsInfo();

        http.request().assertEndpoint("GET", LEGACY_HOST, VENDORS_EP).assertApiKeyInQuery();
        assertEquals(2, info.getVendors().size());
        assertEquals("engine-hours", info.getVendors().get(0).getFeatures().get(1).getSlug());
    }

    @Test
    public void getTelematicsVendorInfoSendsTheVendorId() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, json("{'vendor':{'id':'55','name':'Example Fleet'}}"));

        TelematicsVendorsInfo info = manager.getTelematicsVendorInfo("55");

        http.request().assertEndpoint("GET", LEGACY_HOST, VENDORS_EP).assertQuery("vendor_id", "55");
        assertEquals("Example Fleet", info.getVendor().getName());
    }

    @Test
    public void searchesWithPagingSendTheirOwnParameter() throws Exception {
        TelematicsManager manager = manager();
        http.respondToAll(200, json("{'vendors':[]}"));

        manager.searchVendorByCountry("GB");
        manager.searchVendorByCountry("GB", "2", "10");
        manager.searchVendorBySize("global", "1", "5");
        manager.searchVendorByKeyWord("fleet", "1", "5");
        manager.searchVendorByFeature("satellite", "1", "5");

        http.request(0).assertEndpoint("GET", LEGACY_HOST, VENDORS_EP).assertQuery("country", "GB")
                .assertNoQuery("page");
        http.request(1).assertQuery("country", "GB").assertQuery("page", "2").assertQuery("per_page", "10");
        http.request(2).assertQuery("size", "global").assertNoQuery("country").assertQuery("per_page", "5");
        http.request(3).assertQuery("s", "fleet").assertNoQuery("country");
        http.request(4).assertQuery("feature", "satellite").assertNoQuery("country");
    }

    @Test
    public void oneArgumentSearchesCallTheVendorsEndpoint() throws Exception {
        TelematicsManager manager = manager();
        http.respondToAll(200, Fixtures.read("telematics_vendors_info.json"));

        TelematicsVendorsInfo bySize = manager.searchVendorBySize("global");
        manager.searchVendorByKeyWord("fleet");
        manager.searchVendorByFeature("satellite");

        for (int i = 0; i < 3; i++) {
            http.request(i).assertEndpoint("GET", LEGACY_HOST, VENDORS_EP).assertNoQuery("page");
        }
        assertEquals(2, bySize.getVendors().size());
    }

    @Test
    public void searchVendorBySizeSendsTheSize() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, json("{'vendors':[]}"));

        manager.searchVendorBySize("global");

        http.request().assertQuery("size", "global").assertNoQuery("country");
    }

    @Test
    public void searchVendorByKeyWordSendsTheKeyword() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, json("{'vendors':[]}"));

        manager.searchVendorByKeyWord("fleet");

        http.request().assertQuery("s", "fleet").assertNoQuery("country");
    }

    @Test
    public void searchVendorByFeatureSendsTheFeature() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, json("{'vendors':[]}"));

        manager.searchVendorByFeature("satellite");

        http.request().assertQuery("feature", "satellite").assertNoQuery("country");
    }

    @Test
    public void compareTelematicsVendorsListsTheFeatures() throws Exception {
        TelematicsManager manager = manager();
        http.respondWithFixture(200, "telematics_vendors_info.json");

        List<TelematicsVendorComparison> comparison = manager.compareTelematicsVendors("55,56");

        http.request().assertEndpoint("GET", LEGACY_HOST, VENDORS_EP).assertQuery("vendors", "55,56");
        assertEquals(2, comparison.size());
        assertEquals("55", comparison.get(0).getId());
        assertEquals("Example Fleet", comparison.get(0).getName());
        assertEquals("Satellite,Engine hours", comparison.get(0).getFeatures());
        assertEquals("ID=56 Name=Sample Trackers Features=Satellite", comparison.get(1).toString());
    }

    @Test
    public void compareTelematicsVendorsSkipsMissingData() throws Exception {
        TelematicsManager manager = manager();
        http.respond(200, json("{'vendors':[null,{'id':'57','name':'No Features'}]}")).respondWithoutBody(200);

        List<TelematicsVendorComparison> partial = manager.compareTelematicsVendors("57");
        List<TelematicsVendorComparison> empty = manager.compareTelematicsVendors("58");

        assertEquals(1, partial.size());
        assertEquals("", partial.get(0).getFeatures());
        assertTrue(empty.isEmpty());
    }

    @Test
    public void vendorErrorBecomesApiException() {
        final TelematicsManager manager = manager();
        http.respond(500, json("{'errors':['unavailable']}"));

        http.expectApiException(manager::getTelematicsVendorsInfo);
    }
}
