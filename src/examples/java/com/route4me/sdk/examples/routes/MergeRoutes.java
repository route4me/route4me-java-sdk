package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.MergeRoutesResponse;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.Arrays;

/** Merges two routes into one, keeping the original routes, with {@link RoutingManager#mergeRoutes}. */
public class MergeRoutes {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            MergeRoutesResponse merged = manager.mergeRoutes(Arrays.asList("ROUTE_ID_1_HERE", "ROUTE_ID_2_HERE"),
                    "1604 PARKRIDGE PKWY, Louisville, KY, 40214", false, 38.141598, -85.793846);
            System.out.println("Merged: " + merged.getStatus());
            System.out.println("Optimization of the merged route: " + merged.getOptimizationProblemId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
