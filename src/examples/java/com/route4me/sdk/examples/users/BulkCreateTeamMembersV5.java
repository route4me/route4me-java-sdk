package com.route4me.sdk.examples.users;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.accountprofile.AccountProfileManager;
import com.route4me.sdk.services.users.TeamManagementManagerV5;
import com.route4me.sdk.services.users.TeamMemberRequest;

/**
 * Creates two drivers in one request with {@link TeamManagementManagerV5#bulkCreateTeamMembers}. The owner of the
 * new members is the member of the API key, read with {@link AccountProfileManager#getAccountProfile}.
 */
public class BulkCreateTeamMembersV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TeamManagementManagerV5 manager = new TeamManagementManagerV5(apiKey);
        AccountProfileManager profiles = new AccountProfileManager(apiKey);
        try {
            Long ownerId = profiles.getAccountProfile().getMemberId();
            TeamMemberRequest[] members = new TeamMemberRequest[2];
            for (int i = 0; i < members.length; i++) {
                TeamMemberRequest member = new TeamMemberRequest();
                member.setMemberEmail("driver" + (i + 1) + "@example.com");
                member.setMemberFirstName("Driver");
                member.setMemberLastName("Number " + (i + 1));
                member.setMemberType("SUB_ACCOUNT_DRIVER");
                member.setOwnerMemberId(ownerId);
                members[i] = member;
            }
            AsyncJob job = manager.bulkCreateTeamMembers(members, "skip");
            System.out.println("The API creates the members in background job " + job.getJobId()
                    + "; its status: " + job.getLocation());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
