package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.TeamManagementManagerV5;
import com.route4me.sdk.services.users.TeamMember;
import com.route4me.sdk.services.users.TeamMemberRequest;

import java.util.Collections;

/**
 * Adds skills to a driver, as the C# SDK's {@code AddSkillsToDriver} does, with
 * {@link TeamManagementManagerV5#updateTeamMember}: the skills go in the custom data as {@code driver_skills}.
 */
public class AddSkillsToDriverV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TeamManagementManagerV5 manager = new TeamManagementManagerV5(apiKey);
        try {
            long driverId = 1L;
            TeamMember driver = manager.getTeamMemberById(driverId);
            TeamMemberRequest request = new TeamMemberRequest();
            request.setMemberEmail(driver.getMemberEmail());
            request.setCustomData(Collections.singletonMap("driver_skills", "Forklift,Hazmat"));
            TeamMember updated = manager.updateTeamMember(driverId, request);
            System.out.println("Custom data: " + updated.getCustomData());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
