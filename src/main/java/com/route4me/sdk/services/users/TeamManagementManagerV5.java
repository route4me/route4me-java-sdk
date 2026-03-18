package com.route4me.sdk.services.users;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class TeamManagementManagerV5 extends Manager {
    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String TEAM_USERS_EP = V5_BASE + "/team/users";
    private static final String TEAM_BULK_INSERT_EP = V5_BASE + "/team/bulk-insert";

    public TeamManagementManagerV5(String apiKey) { super(apiKey); }
    public TeamManagementManagerV5(String apiKey, boolean disableRedirects) { super(apiKey, disableRedirects); }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    public TeamMember[] getTeamMembers() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(TEAM_USERS_EP), (HttpEntity) null, TeamMember[].class);
    }

    public TeamMember getTeamMemberById(long userId) throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(TEAM_USERS_EP + "/" + userId), (HttpEntity) null, TeamMember.class);
    }

    public TeamMember createTeamMember(TeamMemberRequest request) throws APIException {
        return this.makeRequest(RequestMethod.POST, v5Builder(TEAM_USERS_EP), this.gson.toJson(request), TeamMember.class);
    }

    public TeamMember updateTeamMember(long userId, TeamMemberRequest request) throws APIException {
        return this.makeRequest(RequestMethod.PUT, v5Builder(TEAM_USERS_EP + "/" + userId), this.gson.toJson(request), TeamMember.class);
    }

    public TeamMember deleteTeamMember(long userId) throws APIException {
        return this.makeRequest(RequestMethod.DELETE, v5Builder(TEAM_USERS_EP + "/" + userId), "", TeamMember.class);
    }

    public void bulkCreateTeamMembers(TeamMemberRequest[] members, String conflictStrategy) throws APIException {
        BulkMembersRequest request = new BulkMembersRequest();
        request.setUsers(members);
        request.setConflicts(conflictStrategy);
        this.makeRequest(RequestMethod.POST, v5Builder(TEAM_BULK_INSERT_EP), this.gson.toJson(request), String.class);
    }
}
