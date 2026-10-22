package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.TeamManagementManagerV5;
import com.route4me.sdk.services.users.TeamMember;
import com.route4me.sdk.services.users.TeamUsersQuery;

import java.util.Arrays;
import java.util.Collections;

/**
 * Lists the drivers of the team, sorted by first name, with
 * {@link TeamManagementManagerV5#getTeamMembers(TeamUsersQuery)}.
 */
public class GetTeamDriversV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TeamManagementManagerV5 manager = new TeamManagementManagerV5(apiKey);
        try {
            TeamUsersQuery query = new TeamUsersQuery().setDriversOnly(true)
                    .setOrderBy(Collections.singletonList(Arrays.asList("member_first_name", "asc")));
            for (TeamMember member : manager.getTeamMembers(query)) {
                System.out.println(member.getMemberId() + " " + member.getMemberFirstName());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
