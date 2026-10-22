package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import com.route4me.sdk.services.users.UsersManager;

/** Deletes a user of the account through the V4 API with {@link UsersManager#userDelete}. */
public class DeleteUser {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        try {
            String memberId = "MEMBER_ID_HERE";
            StatusResponse result = manager.userDelete(memberId);
            System.out.println("Deleted: " + result.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
