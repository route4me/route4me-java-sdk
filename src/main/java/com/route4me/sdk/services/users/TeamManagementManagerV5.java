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
    private static final String TEAM_BULK_INSERT_EP = V5_BASE + "/team/bulk-insert";

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
     * @param request the member
     * @return the created member
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TeamMember createTeamMember(TeamMemberRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(TEAM_USERS_EP), this.gson.toJson(request),
                TeamMember.class);
    }

    /**
     * Updates a team member: {@code PUT https://wh.route4me.com/modules/api/v5.0/team/users/{userId}}, with the request
     * as the JSON body.
     *
     * @param userId the member ID
     * @param request the new values
     * @return the updated member
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TeamMember updateTeamMember(long userId, TeamMemberRequest request) throws APIException {
        return this.makeRequest(RequestMethod.PUT, v5Builder(TEAM_USERS_EP, userId), this.gson.toJson(request),
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
     * Creates several team members: {@code POST https://wh.route4me.com/modules/api/v5.0/team/bulk-insert}, with the
     * members and the conflict rule as the JSON
     * body.
     *
     * @param members the members
     * @param conflictStrategy what to do with a member whose email address already
     *     exists ({@code conflicts})
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public void bulkCreateTeamMembers(TeamMemberRequest[] members, String conflictStrategy) throws APIException {
        BulkMembersRequest request = new BulkMembersRequest();
        request.setUsers(members);
        request.setConflicts(conflictStrategy);
        this.makeRequest(RequestMethod.POST, v5Builder(TEAM_BULK_INSERT_EP), this.gson.toJson(request), String.class);
    }
}
