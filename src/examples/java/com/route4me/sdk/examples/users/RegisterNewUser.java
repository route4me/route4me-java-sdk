/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants.MemberType;
import com.route4me.sdk.services.users.Registration;
import com.route4me.sdk.services.users.UserRegistration;
import com.route4me.sdk.services.users.UsersEnum;
import com.route4me.sdk.services.users.UsersManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Registers a new account with {@link UsersManager#registerUser}, using a manager without an API key. The password of
 * the new account comes from {@code R4M_NEW_ACCOUNT_PASSWORD}.
 */
public class RegisterNewUser {

    public static void main(String[] args) {
        String password = System.getenv("R4M_NEW_ACCOUNT_PASSWORD");
        if (password == null || password.isEmpty()) {
            System.err.println("Set R4M_NEW_ACCOUNT_PASSWORD to the password of the new account.");
            return;
        }
        UserRegistration registration = new UserRegistration();
        registration.setFormat("json");
        registration.setStrEmail("john.java.test@example.com");
        registration.setStrPhone("+1-555-555-0100");
        registration.setStrFirstName("John");
        registration.setStrLastName("Route4Me");
        registration.setStrCompany("Route4Me");
        registration.setStrCompanySize("1-5");
        registration.setIndustry("Java SDK");
        registration.setStrPassword_1(password);
        registration.setStrPassword_2(password);
        registration.setChkTerms("1");
        registration.setPlan(UsersEnum.Plan.MARKETPLACE.getValue());
        registration.setBusinessMemberType(MemberType.PRIMARY_ACCOUNT.getValue());

        UsersManager manager = new UsersManager();

        try {
            Registration r = manager.registerUser(registration);
            System.out.println(r);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(RegisterNewUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIException ex) {
            Logger.getLogger(RegisterNewUser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
