package com.route4me.sdk.examples.users;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.TeamManagementManagerV5;
import com.route4me.sdk.services.users.TeamMember;
public class GetTeamMembersV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TeamManagementManagerV5 manager = new TeamManagementManagerV5(apiKey);
        try {
            TeamMember[] members = manager.getTeamMembers();
            for (TeamMember m : members) {
                System.out.println("Member: " + m.getMemberFirstName() + " " + m.getMemberLastName());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
