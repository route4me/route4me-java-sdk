package com.route4me.sdk.services.users;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import com.route4me.sdk.responses.StatusResponse;

import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link UsersManager}: V4 users and account registration.
 */
public class UsersManagerTest {

    private static final String HOST = "api.route4me.com";
    private static final String USER_EP = "/api.v4/user.php";
    private static final String USER_JSON = json("{'member_id':'1001','member_first_name':'Alex',"
            + "'member_last_name':'Sample','member_email':'alex@example.com','member_type':'SUB_ACCOUNT_DRIVER'}");

    @Rule
    public final MockHttp http = new MockHttp();

    /** Returns a manager that sends its requests to the mocked client. */
    private UsersManager manager() {
        return http.install(new UsersManager(MockHttp.API_KEY));
    }

    /** Builds a sample user with fake data. */
    private static User user() {
        User user = new User();
        user.setMemberFirstName("Alex");
        user.setMemberLastName("Sample");
        user.setMemberEmail("alex@example.com");
        user.setMemberType("SUB_ACCOUNT_DRIVER");
        return user;
    }

    @Test
    @SuppressWarnings("deprecation") // the user.php methods are deprecated (obsolete in C#) but stay tested
    public void createUserPostsTheUser() throws Exception {
        UsersManager manager = manager();
        http.respond(200, USER_JSON);

        User created = manager.createUser(user());

        http.request().assertEndpoint("POST", HOST, USER_EP).assertApiKeyInQuery()
                .assertContentType("application/json")
                .assertJson("member_first_name", "Alex")
                .assertJson("member_email", "alex@example.com")
                .assertJson("member_type", "SUB_ACCOUNT_DRIVER")
                .assertJsonLacks("api_key");
        assertEquals("1001", created.getMemberId());
    }

    @Test
    @SuppressWarnings("deprecation") // the user.php methods are deprecated (obsolete in C#) but stay tested
    public void updateUserPutsTheUser() throws Exception {
        UsersManager manager = manager();
        http.respond(200, USER_JSON);
        User change = user();
        change.setMemberId("1001");

        manager.updateUser(change);

        http.request().assertEndpoint("PUT", HOST, USER_EP).assertJson("member_id", "1001");
    }

    @Test
    @SuppressWarnings("deprecation") // the user.php methods are deprecated (obsolete in C#) but stay tested
    public void getUserSendsTheMemberId() throws Exception {
        UsersManager manager = manager();
        http.respond(200, USER_JSON);

        User user = manager.getUser("1001");

        http.request().assertEndpoint("GET", HOST, USER_EP).assertQuery("member_id", "1001").assertNoBody();
        assertEquals("Sample", user.getMemberLastName());
    }

    @Test
    public void getUsersSendsPaging() throws Exception {
        UsersManager manager = manager();
        http.respond(200, "[" + USER_JSON + "]");

        List<User> users = manager.getUsers(10, 20);

        http.request().assertEndpoint("GET", HOST, "/api/member/view_users.php")
                .assertQuery("limit", "10").assertQuery("offset", "20");
        assertEquals(1, users.size());
    }

    @Test
    public void registerUserPostsAFormWithoutAnApiKey() throws Exception {
        UsersManager manager = http.install(new UsersManager());
        http.respond(200, json("{'status':true,'member_id':1001,'member_email':'alex@example.com',"
                + "'account_type_id':1,'max_routes':10}"));
        UserRegistration registration = new UserRegistration();
        registration.setStrEmail("alex@example.com");
        registration.setStrFirstName("Alex");
        registration.setStrLastName("Sample");
        registration.setStrPassword_1("example-password");
        registration.setStrPassword_2("example-password");
        registration.setPlan(UsersEnum.Plan.MARKETPLACE.toString());
        registration.setChkTerms("1");
        registration.setFormat("json");

        Registration result = manager.registerUser(registration);

        http.request().assertEndpoint("POST", HOST, "/actions/register_action.php")
                .assertNoQuery("api_key")
                .assertQuery("plan", "marketplace")
                .assertContentType("application/x-www-form-urlencoded")
                .assertForm("strEmail", "alex@example.com")
                .assertForm("strFirstName", "Alex")
                .assertForm("chkTerms", "1")
                .assertForm("format", "json");
        assertTrue(result.getStatus());
        assertEquals(Integer.valueOf(1001), result.getMemberID());
        assertEquals(Integer.valueOf(10), result.getMaxRoutes());
    }

    @Test
    @SuppressWarnings("deprecation") // the user.php methods are deprecated (obsolete in C#) but stay tested
    public void userDeleteSendsTheMemberIdInTheQuery() throws Exception {
        UsersManager manager = manager();
        http.respond(200, json("{'status':true}"));

        StatusResponse deleted = manager.userDelete("1001");

        http.request().assertEndpoint("DELETE", HOST, USER_EP).assertApiKeyInQuery()
                .assertQuery("member_id", "1001").assertNoBody();
        assertTrue(deleted.getStatus());
    }

    @Test
    @SuppressWarnings("deprecation") // the user.php methods are deprecated (obsolete in C#) but stay tested
    public void userDeleteRejectsAMissingMemberId() {
        final UsersManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.userDelete(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.userDelete(""));
        http.assertNoRequests();
    }

    @Test
    @SuppressWarnings("deprecation") // the user.php methods are deprecated (obsolete in C#) but stay tested
    public void getUsersWithoutArgumentsListsTheV4Users() throws Exception {
        UsersManager manager = manager();
        http.respond(200, "{\"results\":[" + USER_JSON + "]}");

        UsersResponse users = manager.getUsers();

        http.request().assertEndpoint("GET", HOST, USER_EP).assertApiKeyInQuery()
                .assertNoQuery("member_id").assertNoBody();
        assertEquals("alex@example.com", users.getResults().get(0).getMemberEmail());
    }

    @Test
    public void userAuthenticationPostsTheCredentialsAsAForm() throws Exception {
        UsersManager manager = http.install(new UsersManager());
        http.respond(200, json("{'status':true,'session_id':4321,'session_guid':'SESSION-GUID-1',"
                + "'member_id':1001,'api_key':'" + MockHttp.API_KEY + "','member_type':'PRIMARY_ACCOUNT',"
                + "'max_routes':10,'preferred_units':'MI','auto_logout_ts':1761040000}"));

        MemberResponse session = manager.userAuthentication("alex@example.com", "example-password");

        http.request().assertEndpoint("POST", HOST, "/actions/authenticate.php")
                .assertNoQuery("api_key")
                .assertNoQuery("strPassword")
                .assertContentType("application/x-www-form-urlencoded")
                .assertForm("strEmail", "alex@example.com")
                .assertForm("strPassword", "example-password")
                .assertForm("format", "json");
        assertTrue(session.getStatus());
        assertEquals(Long.valueOf(1001), session.getMemberId());
        assertEquals("SESSION-GUID-1", session.getSessionGuid());
        assertEquals(Integer.valueOf(10), session.getMaxRoutes());
        assertFalse(session.toString().contains(MockHttp.API_KEY));
        assertFalse(session.toString().contains("SESSION-GUID-1"));
        assertFalse(session.toString().contains("4321"));
    }

    @Test
    public void userAuthenticationRejectsMissingCredentials() {
        final UsersManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.userAuthentication(null, "secret"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.userAuthentication("alex@example.com", ""));
        http.assertNoRequests();
    }

    @Test
    public void validateSessionSendsTheSessionInTheQuery() throws Exception {
        UsersManager manager = manager();
        http.respond(200, json("{'authenticated':true,'member_id':1001}"));

        MemberResponse session = manager.validateSession("SESSION-GUID-1", 1001L);

        http.request().assertEndpoint("GET", HOST, "/datafeed/session/validate_session.php").assertApiKeyInQuery()
                .assertQuery("session_guid", "SESSION-GUID-1")
                .assertQuery("member_id", "1001")
                .assertQuery("format", "json")
                .assertNoBody();
        assertTrue(session.getAuthenticated());
        assertEquals(Long.valueOf(1001), session.getMemberId());
    }

    @Test
    public void validateSessionRejectsAMissingSession() {
        final UsersManager manager = manager();

        http.expectThrows(IllegalArgumentException.class, () -> manager.validateSession("", 1001L));
        http.assertNoRequests();
    }

    @Test
    public void getMemberCapabilitiesReadsTheCapabilities() throws Exception {
        UsersManager manager = manager();
        http.respond(200, json("{'avoid':['highways'],'avoid_roads':['tolls'],'features':{'territories':true},"
                + "'travelModes':{'driving':'Driving'},'navigateBy':['address'],'LicensedModules':['ROUTING'],"
                + "'commercial':true}"));

        MemberCapabilities capabilities = manager.getMemberCapabilities();

        http.request().assertEndpoint("GET", HOST, "/api/member/capabilities.php").assertApiKeyInQuery()
                .assertNoQuery("ApiKey").assertNoBody();
        assertEquals("highways", capabilities.getAvoid().get(0));
        assertEquals("tolls", capabilities.getAvoidRoads().get(0));
        assertTrue(capabilities.getFeatures().get("territories"));
        assertEquals("Driving", capabilities.getTravelModes().get("driving"));
        assertEquals("address", capabilities.getNavigateBy().get(0));
        assertEquals("ROUTING", capabilities.getLicensedModules().get(0));
        assertTrue(capabilities.getCommercial());
    }

    @Test
    public void purchaseUserLicensePostsTheLicense() throws Exception {
        UsersManager manager = manager();
        http.respond(200, json("{'status':true}"));
        UserLicenseRequest license = new UserLicenseRequest();
        license.setMemberId(1001L);
        license.setSessionGuid("SESSION-GUID-1");
        license.setDeviceId("DEVICE-1");
        license.setDeviceType("ipad");
        license.setSubscriptionName("IPAD_MONTHLY");
        license.setToken("example-token");
        license.setPayload("example-receipt");
        license.setFormat("json");

        String answer = manager.purchaseUserLicense(license);

        http.request().assertEndpoint("POST", HOST, "/api/member/user_license.php").assertApiKeyInQuery()
                .assertQuery("device_id", "DEVICE-1")
                .assertJson("member_id", 1001)
                .assertJson("session_guid", "SESSION-GUID-1")
                .assertJson("device_id", "DEVICE-1")
                .assertJson("device_type", "ipad")
                .assertJson("subscription_name", "IPAD_MONTHLY")
                .assertJson("token", "example-token")
                .assertJson("payload", "example-receipt")
                .assertJson("format", "json");
        assertTrue(answer.contains("true"));
        assertFalse(license.toString().contains("SESSION-GUID-1"));
        assertFalse(license.toString().contains("example-token"));
        assertFalse(license.toString().contains("example-receipt"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.purchaseUserLicense(null));
    }

    @Test
    public void registerForWebinarPostsTheRegistration() throws Exception {
        UsersManager manager = manager();
        http.respond(200, json("{'status':true}"));
        WebinarRegistration registration = new WebinarRegistration();
        registration.setEmailAddress("alex@example.com");
        registration.setFirstName("Alex");
        registration.setLastName("Sample");
        registration.setPhoneNumber("555-0100");
        registration.setCompanyName("Example Co");
        registration.setMemberId(1001L);
        registration.setWebinarDate("2026-11-05 10:00:00");

        manager.registerForWebinar(registration);

        http.request().assertEndpoint("POST", HOST, "/actions/webinar_register.php").assertApiKeyInQuery()
                .assertContentType("application/json")
                .assertJson("email_address", "alex@example.com")
                .assertJson("first_name", "Alex")
                .assertJson("last_name", "Sample")
                .assertJson("phone_number", "555-0100")
                .assertJson("company_name", "Example Co")
                .assertJson("member_id", 1001)
                .assertJson("webinar_date", "2026-11-05 10:00:00");
        http.expectThrows(IllegalArgumentException.class, () -> manager.registerForWebinar(null));
    }

    @Test
    @SuppressWarnings("deprecation") // the user.php methods are deprecated (obsolete in C#) but stay tested
    public void errorBecomesApiException() {
        final UsersManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getUser("1001"));
    }
}
