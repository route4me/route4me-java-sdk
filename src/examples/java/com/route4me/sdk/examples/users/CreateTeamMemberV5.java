package com.route4me.sdk.examples.users;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.accountprofile.AccountProfileManager;
import com.route4me.sdk.services.users.TeamManagementManagerV5;
import com.route4me.sdk.services.users.TeamMember;
import com.route4me.sdk.services.users.TeamMemberRequest;
/**
 * Creates a team member through the V5 API with {@link TeamManagementManagerV5#createTeamMember}. The owner of the
 * new member is the member of the API key, read with {@link AccountProfileManager#getAccountProfile}; the password
 * of the new member comes from {@code R4M_NEW_MEMBER_PASSWORD}.
 */
public class CreateTeamMemberV5 {
    public static void main(String[] args) {
        String password = System.getenv("R4M_NEW_MEMBER_PASSWORD");
        if (password == null || password.isEmpty()) {
            System.err.println("Set R4M_NEW_MEMBER_PASSWORD to the password of the new member.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TeamManagementManagerV5 manager = new TeamManagementManagerV5(apiKey);
        AccountProfileManager profiles = new AccountProfileManager(apiKey);
        try {
            TeamMemberRequest request = new TeamMemberRequest();
            request.setMemberEmail("test@example.com");
            request.setMemberFirstName("John");
            request.setMemberLastName("Doe");
            request.setMemberPassword(password);
            request.setMemberType("SUB_ACCOUNT_DRIVER");
            request.setOwnerMemberId(profiles.getAccountProfile().getMemberId());
            TeamMember result = manager.createTeamMember(request);
            System.out.println("Created: " + result.getMemberId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
