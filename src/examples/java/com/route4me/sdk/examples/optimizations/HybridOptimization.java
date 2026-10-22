package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.StatusResponse;

import java.util.Collections;

/**
 * Gets the hybrid optimization of a day with {@link RoutingManager#getHybridOptimization}, then replaces its depots
 * with {@link RoutingManager#addDepotsToHybridOptimization}.
 */
public class HybridOptimization {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            DataObject hybrid = manager.getHybridOptimization("2026-10-23", -240);
            System.out.println("Hybrid optimization: " + hybrid.getOptimizationProblemId());

            Address depot = new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 0);
            StatusResponse changed = manager.addDepotsToHybridOptimization(hybrid.getOptimizationProblemId(), true,
                    Collections.singletonList(depot));
            System.out.println("Depots changed: " + changed.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
