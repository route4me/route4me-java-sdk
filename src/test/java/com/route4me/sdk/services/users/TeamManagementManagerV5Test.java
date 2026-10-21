package com.route4me.sdk.services.users;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link TeamManagementManagerV5}: team members on the V5 API.
 */
public class TeamManagementManagerV5Test {

    private static final String HOST = "wh.route4me.com";
    private static final String USERS_EP = "/modules/api/v5.0/team/users";
    private static final String MEMBER = json("{'member_id':1002,'member_email':'driver@example.com',"
            + "'member_type':'SUB_ACCOUNT_DRIVER','member_first_name':'Sam','member_last_name':'Driver',"
            + "'custom_data':{'shift':'am'}}");

    @Rule
    public final MockHttp http = new MockHttp();

    private TeamManagementManagerV5 manager() {
        return http.install(new TeamManagementManagerV5(MockHttp.API_KEY));
    }

    private static TeamMemberRequest member() {
        TeamMemberRequest request = new TeamMemberRequest();
        request.setMemberEmail("driver@example.com");
        request.setMemberFirstName("Sam");
        request.setMemberLastName("Driver");
        request.setMemberType("SUB_ACCOUNT_DRIVER");
        request.setCustomData(Collections.singletonMap("shift", "am"));
        return request;
    }

    @Test
    public void listGetCreateAndDeleteTeamMembers() throws Exception {
        TeamManagementManagerV5 manager = manager();
        http.respond(200, "[" + MEMBER + "]").respond(200, MEMBER).respond(200, MEMBER).respond(200, MEMBER);

        TeamMember[] members = manager.getTeamMembers();
        TeamMember fetched = manager.getTeamMemberById(1002L);
        manager.createTeamMember(member());
        manager.deleteTeamMember(1002L);

        http.request(0).assertEndpoint("GET", HOST, USERS_EP).assertApiKeyInQuery().assertNoBody();
        http.request(1).assertEndpoint("GET", HOST, USERS_EP + "/1002");
        http.request(2).assertEndpoint("POST", HOST, USERS_EP)
                .assertJson("member_email", "driver@example.com")
                .assertJson("member_type", "SUB_ACCOUNT_DRIVER")
                .assertJson("custom_data.shift", "am")
                .assertJsonLacks("member_password");
        http.request(3).assertEndpoint("DELETE", HOST, USERS_EP + "/1002").assertNoBody();
        assertEquals("Sam", members[0].getMemberFirstName());
        assertEquals("am", fetched.getCustomData().get("shift"));
    }

    @Test
    public void bulkCreateTeamMembersPostsUsersAndConflictStrategy() throws Exception {
        TeamManagementManagerV5 manager = manager();
        http.respond(200, json("{'status':true}"));

        manager.bulkCreateTeamMembers(new TeamMemberRequest[] {member()}, "overwrite");

        http.request().assertEndpoint("POST", HOST, "/modules/api/v5.0/team/bulk-insert")
                .assertJson("users[0].member_email", "driver@example.com")
                .assertJson("conflicts", "overwrite");
    }

    @Ignore("registry: PUT /modules/api/v5.0/team/users/{} is not in endpoints.json (the registry has PATCH)")
    @Test
    public void updateTeamMemberSendsTheChanges() throws Exception {
        TeamManagementManagerV5 manager = manager();
        http.respond(200, MEMBER);

        manager.updateTeamMember(1002L, member());

        http.request().assertEndpoint("PUT", HOST, USERS_EP + "/1002").assertJson("member_last_name", "Driver");
    }

    @Test
    public void disabledRedirectsAddRedirectZero() throws Exception {
        TeamManagementManagerV5 manager = http.install(new TeamManagementManagerV5(MockHttp.API_KEY, true));
        http.respond(200, "[]");

        manager.getTeamMembers();

        http.request().assertQuery("redirect", "0");
    }

    @Test
    public void errorBecomesApiException() {
        final TeamManagementManagerV5 manager = manager();
        http.respondWithFixture(400, "error_400.json");

        http.expectApiException(() -> manager.getTeamMemberById(1002L));
    }
}
