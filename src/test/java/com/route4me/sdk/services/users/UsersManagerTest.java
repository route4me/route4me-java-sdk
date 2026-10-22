package com.route4me.sdk.services.users;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
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
    public void updateUserPutsTheUser() throws Exception {
        UsersManager manager = manager();
        http.respond(200, USER_JSON);
        User change = user();
        change.setMemberId("1001");

        manager.updateUser(change);

        http.request().assertEndpoint("PUT", HOST, USER_EP).assertJson("member_id", "1001");
    }

    @Test
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
    public void errorBecomesApiException() {
        final UsersManager manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getUser("1001"));
    }
}
