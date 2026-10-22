package com.route4me.sdk.examples.optimizationprofile;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileManager;

public class DeleteOptimizationProfile {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OptimizationProfileManager manager = new OptimizationProfileManager(apiKey);
        try {
            String profileId = "PROFILE_ID_HERE";
            boolean deleted = manager.deleteOptimizationProfile(profileId);
            System.out.println("Deleted: " + deleted);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
