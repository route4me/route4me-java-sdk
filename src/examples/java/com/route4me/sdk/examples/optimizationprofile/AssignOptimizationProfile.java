package com.route4me.sdk.examples.optimizationprofile;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileAssignResponse;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileManager;

/**
 * Assigns an optimization profile to a group of orders with
 * {@link OptimizationProfileManager#assignOptimizationProfile}. Arguments: the group ID and the profile ID, from
 * {@link GetOptimizationProfiles}.
 */
public class AssignOptimizationProfile {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        String groupId = args.length > 0 ? args[0] : "GROUP_ID_HERE";
        String profileId = args.length > 1 ? args[1] : "PROFILE_ID_HERE";
        OptimizationProfileManager manager = new OptimizationProfileManager(apiKey);
        try {
            OptimizationProfileAssignResponse response = manager.assignOptimizationProfile(groupId, profileId);
            System.out.println("Assigned: " + (response != null ? response.getStatus() : null));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
