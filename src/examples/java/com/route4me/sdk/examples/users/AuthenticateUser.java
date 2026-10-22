package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.MemberResponse;
import com.route4me.sdk.services.users.UsersManager;

/**
 * Logs a member in with {@link UsersManager#userAuthentication} and checks the new session with
 * {@link UsersManager#validateSession}. It reads the email address and the password from the
 * {@code R4M_MEMBER_EMAIL} and {@code R4M_MEMBER_PASSWORD} environment variables, and prints neither the session
 * nor the API key of the member.
 */
public class AuthenticateUser {
    public static void main(String[] args) {
        UsersManager manager = new UsersManager();
        try {
            MemberResponse session = manager.userAuthentication(System.getenv("R4M_MEMBER_EMAIL"),
                    System.getenv("R4M_MEMBER_PASSWORD"));
            System.out.println("Logged in as member " + session.getMemberId());
            MemberResponse check = manager.validateSession(session.getSessionGuid(), session.getMemberId());
            System.out.println("Session valid: " + check.getAuthenticated());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
