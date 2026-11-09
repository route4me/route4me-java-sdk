package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.MergeRoutesResponse;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.Arrays;

/**
 * Merges routes into one, keeping the original routes, with {@link RoutingManager#mergeRoutes}. Set
 * {@code ROUTE_IDS} to the comma-separated IDs of two or more routes of a test account: the example merges them.
 */
public class MergeRoutes {

    public static void main(String[] args) {
        String routeIds = System.getenv("ROUTE_IDS");
        if (routeIds == null || routeIds.trim().isEmpty()) {
            System.err.println("Set ROUTE_IDS to the comma-separated IDs of two or more routes of a test account;"
                    + " this example merges them.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            MergeRoutesResponse merged = manager.mergeRoutes(Arrays.asList(routeIds.trim().split("\\s*,\\s*")),
                    "1604 PARKRIDGE PKWY, Louisville, KY, 40214", false, 38.141598, -85.793846);
            System.out.println("Merged: " + merged.getStatus());
            System.out.println("Optimization of the merged route: " + merged.getOptimizationProblemId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
