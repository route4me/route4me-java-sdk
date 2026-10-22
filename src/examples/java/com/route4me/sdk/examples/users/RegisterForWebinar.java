package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.UsersManager;
import com.route4me.sdk.services.users.WebinarRegistration;

/** Registers a person for a webinar with {@link UsersManager#registerForWebinar}. */
public class RegisterForWebinar {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        try {
            WebinarRegistration registration = new WebinarRegistration();
            registration.setEmailAddress("alex@example.com");
            registration.setFirstName("Alex");
            registration.setLastName("Sample");
            registration.setPhoneNumber("555-0100");
            registration.setCompanyName("Example Co");
            registration.setMemberId(1L);
            registration.setWebinarDate("2026-11-05 10:00:00");
            System.out.println(manager.registerForWebinar(registration));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
