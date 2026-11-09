package com.route4me.sdk.examples.optimizationprofile;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileAssignResponse;
import com.route4me.sdk.services.optimizationprofile.OptimizationProfileManager;

/**
 * Assigns an optimization profile to a group of orders with
 * {@link OptimizationProfileManager#assignOptimizationProfile}. Arguments: the group ID and the profile ID, from
 * {@link GetOptimizationProfiles}; without them, the example reads {@code ORDER_GROUP_ID} and
 * {@code OPTIMIZATION_PROFILE_ID}. Use a group of orders of a test account: the example changes it.
 */
public class AssignOptimizationProfile {
    public static void main(String[] args) {
        String groupId = args.length > 0 ? args[0] : System.getenv("ORDER_GROUP_ID");
        String profileId = args.length > 1 ? args[1] : System.getenv("OPTIMIZATION_PROFILE_ID");
        if (groupId == null || groupId.isEmpty() || profileId == null || profileId.isEmpty()) {
            System.err.println("Pass the group ID and the profile ID as arguments, or set ORDER_GROUP_ID and"
                    + " OPTIMIZATION_PROFILE_ID to a group of orders and an optimization profile of a test account;"
                    + " this example assigns the profile to the group.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OptimizationProfileManager manager = new OptimizationProfileManager(apiKey);
        try {
            OptimizationProfileAssignResponse response = manager.assignOptimizationProfile(groupId, profileId);
            System.out.println("Assigned: " + (response != null ? response.getStatus() : null));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
