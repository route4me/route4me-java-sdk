
package com.route4me.sdk.examples.users;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.services.users.UsersManager;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.services.users.User;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetUsers {
   public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        UsersManager userManager = route4me.getUserManager();
        List<User> users = userManager.getUsers(10, 5);
        for (User user: users){
            System.out.println("Member ID: " + user.getMember_id());
            System.out.println("Member First Name: " + user.getMember_first_name());
            System.out.println("Member Last Name: " + user.getMember_last_name());
            System.out.println("Member Email: " + user.getMember_email());
            System.out.println();
        }

        
    }        
}
