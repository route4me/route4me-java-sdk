package com.route4me.sdk.examples.optimizationprofile;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfile;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileCreateRequest;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileManager;

public class CreateOptimizationProfile {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OptimizationProfileManager manager = new OptimizationProfileManager(apiKey);
        try {
            OptimizationProfileCreateRequest request = new OptimizationProfileCreateRequest();
            request.setProfileName("Test Profile");
            request.setAvoidHighways(false);
            request.setAvoidTolls(false);
            OptimizationProfile profile = manager.createOptimizationProfile(request);
            System.out.println("Created profile: " + profile.getProfileName());
            System.out.println("Profile ID: " + profile.getOptimizationProfileId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
