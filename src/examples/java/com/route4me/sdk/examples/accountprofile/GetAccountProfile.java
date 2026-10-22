package com.route4me.sdk.examples.accountprofile;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.accountprofile.AccountProfile;
import com.route4me.sdk.services.accountprofile.AccountProfileManager;

/** Gets the profile of the account with {@link AccountProfileManager#getAccountProfile}. */
public class GetAccountProfile {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        AccountProfileManager manager = new AccountProfileManager(apiKey);
        try {
            AccountProfile profile = manager.getAccountProfile();
            System.out.println("Email: " + profile.getEmail());
            System.out.println("Member ID: " + profile.getMemberId());
            System.out.println("Preferred Units: " + profile.getPreferredUnits());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
