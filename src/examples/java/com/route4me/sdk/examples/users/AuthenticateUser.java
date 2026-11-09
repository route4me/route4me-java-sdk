package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.MemberResponse;
import com.route4me.sdk.services.users.UsersManager;

/**
 * Logs a member in with {@link UsersManager#userAuthentication} and checks the new session with
 * {@link UsersManager#validateSession}. It reads the email address and the password of a member of a test account
 * from the {@code R4M_MEMBER_EMAIL} and {@code R4M_MEMBER_PASSWORD} environment variables, stops when either is unset,
 * and prints neither the session nor the API key of the member.
 */
public class AuthenticateUser {
    public static void main(String[] args) {
        String email = System.getenv("R4M_MEMBER_EMAIL");
        String password = System.getenv("R4M_MEMBER_PASSWORD");
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            System.err.println("Set R4M_MEMBER_EMAIL and R4M_MEMBER_PASSWORD to a member of a test account; this"
                    + " example logs that member in.");
            return;
        }
        UsersManager manager = new UsersManager();
        try {
            MemberResponse session = manager.userAuthentication(email, password);
            if (session.getSessionGuid() == null || session.getMemberId() == null) {
                System.out.println("The login failed: the answer has no session");
                return;
            }
            System.out.println("Logged in as member " + session.getMemberId());
            MemberResponse check = manager.validateSession(session.getSessionGuid(), session.getMemberId());
            System.out.println("Session valid: " + check.getAuthenticated());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
