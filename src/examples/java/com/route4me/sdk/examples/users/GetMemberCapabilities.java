package com.route4me.sdk.examples.users;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.users.MemberCapabilities;
import com.route4me.sdk.services.users.UsersManager;

/** Prints the capabilities of the account with {@link UsersManager#getMemberCapabilities}. */
public class GetMemberCapabilities {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        UsersManager manager = new UsersManager(apiKey);
        try {
            MemberCapabilities capabilities = manager.getMemberCapabilities();
            System.out.println("Commercial: " + capabilities.getCommercial());
            System.out.println("Licensed modules: " + capabilities.getLicensedModules());
            System.out.println("Features: " + capabilities.getFeatures());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
