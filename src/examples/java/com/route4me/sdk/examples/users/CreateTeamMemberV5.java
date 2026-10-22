package com.route4me.sdk.examples.users;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.accountprofile.AccountProfileManager;
import com.route4me.sdk.services.users.TeamManagementManagerV5;
import com.route4me.sdk.services.users.TeamMember;
import com.route4me.sdk.services.users.TeamMemberRequest;
/**
 * Creates a team member through the V5 API with {@link TeamManagementManagerV5#createTeamMember}. The owner of the
 * new member is the member of the API key, read with {@link AccountProfileManager#getAccountProfile}.
 */
public class CreateTeamMemberV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TeamManagementManagerV5 manager = new TeamManagementManagerV5(apiKey);
        AccountProfileManager profiles = new AccountProfileManager(apiKey);
        try {
            TeamMemberRequest request = new TeamMemberRequest();
            request.setMemberEmail("test@example.com");
            request.setMemberFirstName("John");
            request.setMemberLastName("Doe");
            request.setMemberPassword("testpassword123");
            request.setMemberType("SUB_ACCOUNT_DRIVER");
            request.setOwnerMemberId(profiles.getAccountProfile().getMemberId());
            TeamMember result = manager.createTeamMember(request);
            System.out.println("Created: " + result.getMemberId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
