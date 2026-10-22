package com.route4me.sdk.examples.optimizationprofile;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfile;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileManager;

public class GetOptimizationProfileById {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OptimizationProfileManager manager = new OptimizationProfileManager(apiKey);
        try {
            String profileId = "PROFILE_ID_HERE";
            OptimizationProfile profile = manager.getOptimizationProfileById(profileId);
            System.out.println("Profile: " + profile.getProfileName());
            System.out.println("Default: " + profile.getIsDefault());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
