package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.User;
import com.route4me.sdk.services.users.UsersManager;

import java.util.List;

/**
 * @author juan
 */
public class GetUsers {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        UsersManager manager = new UsersManager(apiKey);
        try {
            List<User> users = manager.getUsers(10, 10);
            for (User user : users) {
                System.out.println(user);
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
