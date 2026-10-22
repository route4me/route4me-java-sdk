/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.User;
import com.route4me.sdk.services.users.UsersManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads a user with {@link UsersManager#getUser} and changes its phone number with {@link UsersManager#updateUser}. Set
 * {@code MEMBER_ID} to a member of a test account.
 */
public class UpdateUser {

    public static void main(String[] args) {
        String memberId = System.getenv("MEMBER_ID");
        if (memberId == null || memberId.isEmpty()) {
            System.err.println("Set MEMBER_ID to a member of a test account; this example changes its phone number.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        User user;
        try {
            user = manager.getUser(memberId);
            System.out.println(user);
        } catch (APIException ex) {
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if (user == null) {
            System.err.println("No member has the ID " + memberId);
            return;
        }
        user.setMemberPhone("+1-555-555-0100");
        try {
            user = manager.updateUser(user);
            System.out.println(user);
        } catch (APIException ex) {
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
