package com.route4me.sdk.services.users;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/** Manages the members of the team through the V5 API ({@code https://wh.route4me.com/modules/api/v5.0/team}). */
public class TeamManagementManagerV5 extends Manager {
    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String TEAM_USERS_EP = V5_BASE + "/team/users";
    /** The bulk insert route, as C# has it; the spec's {@code /team/users/bulk-insert} answers 405. */
    private static final String TEAM_BULK_INSERT_EP = V5_BASE + "/team/bulk-insert";
    /** The most members one bulk insert takes, as the spec says. */
    private static final int MAX_BULK_MEMBERS = 100;

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public TeamManagementManagerV5(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public TeamManagementManagerV5(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    /**
     * Lists the team members: {@code GET https://wh.route4me.com/modules/api/v5.0/team/users}.
     *
     * @return the members
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TeamMember[] getTeamMembers() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(TEAM_USERS_EP), (HttpEntity) null, TeamMember[].class);
    }

    /**
     * Lists the team members that match filters: {@code GET https://wh.route4me.com/modules/api/v5.0/team/users},
     * with the filters as the query ({@code query}, {@code drivers_only}, {@code optimization_problem_id},
     * {@code order_by[i][]} and {@code facility_ids[]}), as the C# SDK ({@code GetTeamMembers}) sends them.
     *
     * @param query the filters; {@code null}, or a field left {@code null}, sends no filter
     * @return the members
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TeamMember[] getTeamMembers(TeamUsersQuery query) throws APIException {
        return this.makeJSONRequest(RequestMethod.GET, v5Builder(TEAM_USERS_EP), query, TeamMember[].class);
    }

    /**
     * Gets one team member: {@code GET https://wh.route4me.com/modules/api/v5.0/team/users/{userId}}.
     *
     * @param userId the member ID
     * @return the member
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TeamMember getTeamMemberById(long userId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(TEAM_USERS_EP, userId), (HttpEntity) null,
                TeamMember.class);
    }

    /**
     * Creates a team member: {@code POST https://wh.route4me.com/modules/api/v5.0/team/users}, with the request as the
     * JSON body.
     *
     * @param request the member; the spec requires {@code member_first_name}, {@code member_last_name},
     *     {@code member_email}, {@code member_type} and {@code OWNER_MEMBER_ID}
     * @return the created member
     * @throws IllegalArgumentException if {@code request} is {@code null} or a required field is missing
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TeamMember createTeamMember(TeamMemberRequest request) throws APIException {
        requireMember(request);
        require(request.getMemberFirstName(), "member_first_name");
        require(request.getMemberLastName(), "member_last_name");
        require(request.getMemberEmail(), "member_email");
        require(request.getMemberType(), "member_type");
        if (request.getOwnerMemberId() == null) {
            throw new IllegalArgumentException("The member needs OWNER_MEMBER_ID");
        }
        return this.makeRequest(RequestMethod.POST, v5Builder(TEAM_USERS_EP), this.gson.toJson(request),
                TeamMember.class);
    }

    /**
     * Updates a team member: {@code PATCH https://wh.route4me.com/modules/api/v5.0/team/users/{userId}}, with the
     * request as the JSON body. The spec and the C# SDK ({@code UpdateTeamMember}) send {@code PATCH}.
     *
     * <p>To add skills to a driver, as the C# SDK's {@code AddSkillsToDriver} does, send them in the custom data,
     * separated by commas: {@code request.setCustomData(Collections.singletonMap("driver_skills", "Forklift,Hazmat"))}.
     *
     * @param userId the member ID
     * @param request the new values; the spec requires {@code member_email}
     * @return the updated member
     * @throws IllegalArgumentException if {@code request} is {@code null} or has no {@code member_email}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TeamMember updateTeamMember(long userId, TeamMemberRequest request) throws APIException {
        requireMember(request);
        require(request.getMemberEmail(), "member_email");
        return this.makeRequest(RequestMethod.PATCH, v5Builder(TEAM_USERS_EP, userId), this.gson.toJson(request),
                TeamMember.class);
    }

    /**
     * Deletes a team member: {@code DELETE https://wh.route4me.com/modules/api/v5.0/team/users/{userId}}.
     *
     * @param userId the member ID
     * @return the deleted member
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TeamMember deleteTeamMember(long userId) throws APIException {
        return this.makeRequest(RequestMethod.DELETE, v5Builder(TEAM_USERS_EP, userId), "", TeamMember.class);
    }

    /**
     * Creates up to 100 team members in one request: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/team/bulk-insert}, with the members and the conflict rule as the
     * JSON body. This is the path the C# SDK uses; the spec's {@code /team/users/bulk-insert} answers 405.
     *
     * <p>The API creates the members in a background job. It answers with an empty object, and with the job's status
     * URL in its {@code Location} header, which this method does not return; list the members with
     * {@link #getTeamMembers()} to see the result.
     *
     * @param members the members, from 1 to 100; each one needs {@code OWNER_MEMBER_ID} or {@code owner_email}
     * @param conflictStrategy what to do with a member whose email address already exists: {@code fail},
     *     {@code overwrite} or {@code skip} ({@code conflicts}); {@code null} leaves it to the API
     * @throws IllegalArgumentException if {@code members} is {@code null}, empty, holds a {@code null} member or holds
     *     more than 100 members
     * @throws APIException if the request fails or the server answers with an error status
     */
    public void bulkCreateTeamMembers(TeamMemberRequest[] members, String conflictStrategy) throws APIException {
        if (members == null || members.length == 0 || members.length > MAX_BULK_MEMBERS) {
            throw new IllegalArgumentException("A bulk insert takes from 1 to " + MAX_BULK_MEMBERS + " members");
        }
        for (TeamMemberRequest member : members) {
            requireMember(member);
        }
        BulkMembersRequest request = new BulkMembersRequest();
        request.setUsers(members);
        request.setConflicts(conflictStrategy);
        this.makeRequest(RequestMethod.POST, v5Builder(TEAM_BULK_INSERT_EP), this.gson.toJson(request), String.class);
    }

    /** Rejects a {@code null} member before any request is sent. */
    private static void requireMember(TeamMemberRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("The member must not be null");
        }
    }

    /** Rejects a missing or empty required field before any request is sent. */
    private static void require(String value, String field) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("The member needs " + field);
        }
    }
}
