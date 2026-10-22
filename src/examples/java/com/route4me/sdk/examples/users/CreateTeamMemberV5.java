package com.route4me.sdk.examples.users;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.TeamManagementManagerV5;
import com.route4me.sdk.services.users.TeamMember;
import com.route4me.sdk.services.users.TeamMemberRequest;
public class CreateTeamMemberV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TeamManagementManagerV5 manager = new TeamManagementManagerV5(apiKey);
        try {
            TeamMemberRequest request = new TeamMemberRequest();
            request.setMemberEmail("test@example.com");
            request.setMemberFirstName("John");
            request.setMemberLastName("Doe");
            request.setMemberPassword("testpassword123");
            request.setMemberType("SUB_ACCOUNT_DRIVER");
            TeamMember result = manager.createTeamMember(request);
            System.out.println("Created: " + result.getMemberId());
        } catch (APIException e) { e.printStackTrace(); }
    }
}
