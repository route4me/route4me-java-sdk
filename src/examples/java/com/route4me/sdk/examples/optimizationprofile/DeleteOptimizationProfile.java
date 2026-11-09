package com.route4me.sdk.examples.optimizationprofile;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileManager;

/**
 * Deletes an optimization profile with {@link OptimizationProfileManager#deleteOptimizationProfile}. Set
 * {@code OPTIMIZATION_PROFILE_ID} to an optimization profile of a test account: the example deletes it.
 */
public class DeleteOptimizationProfile {
    public static void main(String[] args) {
        String profileId = System.getenv("OPTIMIZATION_PROFILE_ID");
        if (profileId == null || profileId.isEmpty()) {
            System.err.println("Set OPTIMIZATION_PROFILE_ID to the ID of an optimization profile of a test account;"
                    + " this example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OptimizationProfileManager manager = new OptimizationProfileManager(apiKey);
        try {
            boolean deleted = manager.deleteOptimizationProfile(profileId);
            System.out.println("Deleted: " + deleted);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
