package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.MemberResponse;
import com.route4me.sdk.services.users.UsersManager;

/**
 * Checks whether a member session is still valid with {@link UsersManager#validateSession}. It reads the session
 * GUID from the {@code R4M_SESSION_GUID} environment variable.
 */
public class ValidateSession {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        try {
            long memberId = 1L;
            MemberResponse session = manager.validateSession(System.getenv("R4M_SESSION_GUID"), memberId);
            System.out.println("Session valid: " + session.getAuthenticated());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
