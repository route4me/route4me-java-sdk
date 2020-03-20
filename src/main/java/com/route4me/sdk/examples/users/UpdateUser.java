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
 *
 * @author juan
 */
public class UpdateUser {

    public static void main(String[] args) {

        String apiKey = "11111111111111111111111111111111";
        UsersManager manager = new UsersManager(apiKey);
        User user = null;
        try {
            user = manager.getUser("1");
            System.out.println(user);
        } catch (APIException ex) {
            Logger.getLogger(GetUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        user.setMemberPhone("123456789");
        try {
            user = manager.updateUser(user);
            System.out.println(user);
        } catch (APIException ex) {
            Logger.getLogger(UpdateUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
