package com.route4me.sdk.examples.users;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.TeamManagementManagerV5;
import com.route4me.sdk.services.users.TeamMember;

/**
 * Deletes a member of the team with {@link TeamManagementManagerV5#deleteTeamMember}, which replaces the deprecated
 * {@code UsersManager.userDelete} of the V4 {@code user.php} endpoint. Set {@code MEMBER_ID} to a member of a test
 * account: the example deletes it.
 */
public class DeleteUser {
    public static void main(String[] args) {
        String memberId = System.getenv("MEMBER_ID");
        if (memberId == null || memberId.isEmpty()) {
            System.err.println("Set MEMBER_ID to a member of a test account; this example deletes it.");
            return;
        }
        Long id = ExampleInput.toLong("MEMBER_ID", memberId);
        if (id == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TeamManagementManagerV5 manager = new TeamManagementManagerV5(apiKey);
        try {
            TeamMember deleted = manager.deleteTeamMember(id);
            System.out.println("Deleted: " + (deleted == null ? memberId : deleted.getMemberId()));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
