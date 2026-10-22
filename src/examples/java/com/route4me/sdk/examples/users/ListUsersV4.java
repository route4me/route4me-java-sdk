package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.User;
import com.route4me.sdk.services.users.UsersManager;
import com.route4me.sdk.services.users.UsersResponse;

/** Lists the users of the account from {@code /api.v4/user.php} with {@link UsersManager#getUsers()}. */
public class ListUsersV4 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        try {
            UsersResponse users = manager.getUsers();
            if (users.getResults() != null) {
                for (User user : users.getResults()) {
                    System.out.println(user.getMemberId() + " " + user.getMemberEmail());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
