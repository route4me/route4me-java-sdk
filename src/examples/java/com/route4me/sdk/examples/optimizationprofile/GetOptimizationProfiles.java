package com.route4me.sdk.examples.optimizationprofile;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfile;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileManager;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfilesResponse;

public class GetOptimizationProfiles {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OptimizationProfileManager manager = new OptimizationProfileManager(apiKey);
        try {
            OptimizationProfilesResponse profiles = manager.getOptimizationProfiles();
            if (profiles.getItems() != null) {
                for (OptimizationProfile profile : profiles.getItems()) {
                    System.out.println("Profile: " + profile.getProfileName() + " (ID: "
                            + profile.getOptimizationProfileId() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
