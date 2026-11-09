package com.route4me.sdk.examples.users;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.UsersManager;
import com.route4me.sdk.services.users.WebinarRegistration;

/**
 * Registers a person for a webinar with {@link UsersManager#registerForWebinar}. Set {@code MEMBER_ID} to a member of
 * a test account: the example registers it.
 */
public class RegisterForWebinar {
    public static void main(String[] args) {
        String memberId = System.getenv("MEMBER_ID");
        if (memberId == null || memberId.isEmpty()) {
            System.err.println("Set MEMBER_ID to a member of a test account; this example registers it for a"
                    + " webinar.");
            return;
        }
        Long id = ExampleInput.toLong("MEMBER_ID", memberId);
        if (id == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        try {
            WebinarRegistration registration = new WebinarRegistration();
            registration.setEmailAddress("alex@example.com");
            registration.setFirstName("Alex");
            registration.setLastName("Sample");
            registration.setPhoneNumber("555-0100");
            registration.setCompanyName("Example Co");
            registration.setMemberId(id);
            registration.setWebinarDate("2026-11-05 10:00:00");
            System.out.println(manager.registerForWebinar(registration));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
