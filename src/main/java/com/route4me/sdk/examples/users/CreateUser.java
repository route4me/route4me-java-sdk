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

/**
 *
 * @author juan
 */
public class CreateUser {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        User user = new User();
        user.setMemberFirstName("Clay");
        user.setMemberLastName("Abraham");
        user.setMemberEmail("sssss+newdispatcher@gmail.com");
        user.setMemberPassword("123456Secret");
        user.setDateOfBirth("1978");
        user.setMemberPhone("571-22-5555");
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
