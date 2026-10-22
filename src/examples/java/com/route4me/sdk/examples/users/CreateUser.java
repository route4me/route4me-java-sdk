/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.users.User;
import com.route4me.sdk.services.users.UsersManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Creates a user of the account with {@link UsersManager#createUser}. */
public class CreateUser {
    public static void main(String[] args) {
        String password = System.getenv("R4M_NEW_MEMBER_PASSWORD");
        if (password == null || password.isEmpty()) {
            System.err.println("Set R4M_NEW_MEMBER_PASSWORD to the password of the new user.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        User user = new User();
        user.setMemberFirstName("Clay");
        user.setMemberLastName("Abraham");
        user.setMemberEmail("new.dispatcher@example.com");
        user.setMemberPassword(password);
        user.setDateOfBirth("1978");
        user.setMemberPhone("+1-555-555-0100");
        user.setMemberZipcode("22102");
        user.setMemberType(Constants.MemberType.SUB_ACCOUNT_DRIVER.toString());
        user.setHideRoutedAddresses(Constants.MemberPropertyValue.FALSE.toString());
        user.setHideVisitedAddresses(Constants.MemberPropertyValue.FALSE.toString());
        user.setReadonlyUser(Constants.MemberPropertyValue.FALSE.toString());
        UsersManager manager = new UsersManager(apiKey);
        try {
            user = manager.createUser(user);
        } catch (APIException ex) {
            Logger.getLogger(CreateUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(user);

    }

}
