package com.route4me.sdk.examples.optimizationprofile;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfile;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileManager;

/**
 * Gets one optimization profile with {@link OptimizationProfileManager#getOptimizationProfileById}. Set
 * {@code OPTIMIZATION_PROFILE_ID} to one of your optimization profiles.
 */
public class GetOptimizationProfileById {
    public static void main(String[] args) {
        String profileId = System.getenv("OPTIMIZATION_PROFILE_ID");
        if (profileId == null || profileId.isEmpty()) {
            System.err.println("Set OPTIMIZATION_PROFILE_ID to the ID of one of your optimization profiles; this"
                    + " example reads it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OptimizationProfileManager manager = new OptimizationProfileManager(apiKey);
        try {
            OptimizationProfile profile = manager.getOptimizationProfileById(profileId);
            System.out.println("Profile: " + profile.getProfileName());
            System.out.println("Default: " + profile.getIsDefault());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
