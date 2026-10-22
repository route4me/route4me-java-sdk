package com.route4me.sdk.services.users;

import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.route4me.sdk.testsupport.MockHttp.json;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

    /** Returns a manager that sends its requests to the mocked client. */
    private TeamManagementManagerV5 manager() {
        return http.install(new TeamManagementManagerV5(MockHttp.API_KEY));
    }

    /** Builds a sample team member request with fake data. */
    private static TeamMemberRequest member() {
        TeamMemberRequest request = new TeamMemberRequest();
        request.setMemberEmail("driver@example.com");
        request.setMemberFirstName("Sam");
        request.setMemberLastName("Driver");
        request.setMemberType("SUB_ACCOUNT_DRIVER");
        request.setOwnerMemberId(1001L);
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

        http.request(0).assertEndpoint("GET", HOST, USERS_EP).assertApiKeyAsBearer().assertNoBody();
        http.request(1).assertEndpoint("GET", HOST, USERS_EP + "/1002");
        http.request(2).assertEndpoint("POST", HOST, USERS_EP)
                .assertJson("member_email", "driver@example.com")
                .assertJson("member_type", "SUB_ACCOUNT_DRIVER")
                .assertJson("custom_data.shift", "am")
                .assertJson("OWNER_MEMBER_ID", 1001)
                .assertJsonLacks("member_password", "new_password");
        http.request(3).assertEndpoint("DELETE", HOST, USERS_EP + "/1002").assertNoBody();
        assertEquals("Sam", members[0].getMemberFirstName());
        assertEquals("am", fetched.getCustomData().get("shift"));
    }

    @Test
    public void getTeamMembersSendsTheFiltersWithBrackets() throws Exception {
        TeamManagementManagerV5 manager = manager();
        http.respondToAll(200, "[" + MEMBER + "]");
        TeamUsersQuery query = new TeamUsersQuery().setQuery("driver@example.com").setDriversOnly(true)
                .setOptimizationProblemId("OPT-1")
                .setOrderBy(Collections.singletonList(Arrays.asList("member_first_name", "asc")))
                .setFacilityIds(Arrays.asList("FAC-1", "FAC-2"));

        TeamMember[] members = manager.getTeamMembers(query);
        manager.getTeamMembers((TeamUsersQuery) null);

        http.request(0).assertEndpoint("GET", HOST, USERS_EP).assertApiKeyAsBearer().assertNoBody()
                .assertQuery("query", "driver@example.com")
                .assertQuery("drivers_only", "1")
                .assertQuery("optimization_problem_id", "OPT-1")
                .assertQueryValues("order_by[0][]", "member_first_name", "asc")
                .assertQueryValues("facility_ids[]", "FAC-1", "FAC-2")
                .assertNoQuery("facility_ids");
        http.request(1).assertEndpoint("GET", HOST, USERS_EP).assertNoQuery("query").assertNoQuery("drivers_only");
        assertEquals(Long.valueOf(1002), members[0].getMemberId());
    }

    @Test
    public void createTeamMemberSendsTheOwnerAndThePasswordAsTheSpecNamesThem() throws Exception {
        TeamManagementManagerV5 manager = manager();
        http.respond(200, MEMBER);
        TeamMemberRequest request = member();
        request.setMemberPassword("example-password");

        manager.createTeamMember(request);

        http.request().assertEndpoint("POST", HOST, USERS_EP)
                .assertJson("OWNER_MEMBER_ID", 1001)
                .assertJson("new_password", "example-password")
                .assertJsonLacks("member_password");
        assertFalse(request.toString().contains("example-password"));
    }

    @Test
    public void createTeamMemberRequiresTheSpecFields() {
        final TeamManagementManagerV5 manager = manager();
        final TeamMemberRequest noOwner = member();
        noOwner.setOwnerMemberId(null);
        final TeamMemberRequest noEmail = member();
        noEmail.setMemberEmail("");
        final TeamMemberRequest noType = member();
        noType.setMemberType(null);
        final TeamMemberRequest noFirstName = member();
        noFirstName.setMemberFirstName(null);
        final TeamMemberRequest noLastName = member();
        noLastName.setMemberLastName(null);

        http.expectThrows(IllegalArgumentException.class, () -> manager.createTeamMember(null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createTeamMember(noOwner));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createTeamMember(noEmail));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createTeamMember(noType));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createTeamMember(noFirstName));
        http.expectThrows(IllegalArgumentException.class, () -> manager.createTeamMember(noLastName));
        http.assertNoRequests();
    }

    @Test
    public void bulkCreateTeamMembersPostsUsersAndConflictStrategy() throws Exception {
        TeamManagementManagerV5 manager = manager();
        http.respond(200, "{}").respond(200, "{}");

        manager.bulkCreateTeamMembers(new TeamMemberRequest[] {member()}, "overwrite");
        manager.bulkCreateTeamMembers(new TeamMemberRequest[] {member()}, null);

        http.request(0).assertEndpoint("POST", HOST, USERS_EP + "/bulk-insert")
                .assertJson("users[0].member_email", "driver@example.com")
                .assertJson("users[0].OWNER_MEMBER_ID", 1001)
                .assertJson("conflicts", "overwrite");
        http.request(1).assertJsonLacks("conflicts");
    }

    @Test
    public void bulkCreateTeamMembersAcceptsAnEmptyAcceptedReply() throws Exception {
        TeamManagementManagerV5 manager = manager();
        http.respondWithoutBody(202);

        manager.bulkCreateTeamMembers(new TeamMemberRequest[] {member()}, "skip");

        http.request().assertEndpoint("POST", HOST, USERS_EP + "/bulk-insert");
    }

    @Test
    public void bulkCreateTeamMembersRejectsAnEmptyOrTooLargeList() {
        final TeamManagementManagerV5 manager = manager();
        final TeamMemberRequest[] tooMany = new TeamMemberRequest[101];
        Arrays.fill(tooMany, member());

        http.expectThrows(IllegalArgumentException.class, () -> manager.bulkCreateTeamMembers(null, "skip"));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.bulkCreateTeamMembers(new TeamMemberRequest[0], "skip"));
        http.expectThrows(IllegalArgumentException.class, () -> manager.bulkCreateTeamMembers(tooMany, "skip"));
        http.expectThrows(IllegalArgumentException.class,
                () -> manager.bulkCreateTeamMembers(new TeamMemberRequest[] {null}, "skip"));
        http.assertNoRequests();
    }

    @Test
    public void updateTeamMemberPatchesTheChanges() throws Exception {
        TeamManagementManagerV5 manager = manager();
        http.respond(200, MEMBER);

        manager.updateTeamMember(1002L, member());

        http.request().assertEndpoint("PATCH", HOST, USERS_EP + "/1002")
                .assertJson("member_last_name", "Driver")
                .assertJson("member_email", "driver@example.com");
    }

    @Test
    public void updateTeamMemberAddsDriverSkillsThroughTheCustomData() throws Exception {
        TeamManagementManagerV5 manager = manager();
        http.respond(200, MEMBER);
        TeamMemberRequest skills = new TeamMemberRequest();
        skills.setMemberEmail("driver@example.com");
        skills.setCustomData(Collections.singletonMap("driver_skills", "Forklift,Hazmat"));

        manager.updateTeamMember(1002L, skills);

        http.request().assertEndpoint("PATCH", HOST, USERS_EP + "/1002")
                .assertJson("custom_data.driver_skills", "Forklift,Hazmat")
                .assertJsonLacks("member_first_name", "OWNER_MEMBER_ID");
    }

    @Test
    public void updateTeamMemberRequiresTheEmail() {
        final TeamManagementManagerV5 manager = manager();
        final TeamMemberRequest noEmail = member();
        noEmail.setMemberEmail(null);

        http.expectThrows(IllegalArgumentException.class, () -> manager.updateTeamMember(1002L, null));
        http.expectThrows(IllegalArgumentException.class, () -> manager.updateTeamMember(1002L, noEmail));
        http.assertNoRequests();
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
