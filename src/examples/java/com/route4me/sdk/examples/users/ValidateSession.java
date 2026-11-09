package com.route4me.sdk.examples.users;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.MemberResponse;
import com.route4me.sdk.services.users.UsersManager;

/**
 * Checks whether a member session is still valid with {@link UsersManager#validateSession}. It reads the session
 * GUID from the {@code R4M_SESSION_GUID} environment variable and the ID of the member of the session from
 * {@code MEMBER_ID}.
 */
public class ValidateSession {
    public static void main(String[] args) {
        String sessionGuid = System.getenv("R4M_SESSION_GUID");
        String memberId = System.getenv("MEMBER_ID");
        if (sessionGuid == null || sessionGuid.isEmpty() || memberId == null || memberId.isEmpty()) {
            System.err.println("Set R4M_SESSION_GUID to a session GUID and MEMBER_ID to the ID of the member of that"
                    + " session; this example checks whether the session is valid.");
            return;
        }
        Long id = ExampleInput.toLong("MEMBER_ID", memberId);
        if (id == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        try {
            MemberResponse session = manager.validateSession(sessionGuid, id);
            System.out.println("Session valid: " + session.getAuthenticated());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
