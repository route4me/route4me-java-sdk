package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.StatusResponse;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;

/**
 * Gets the hybrid optimization of a day with {@link RoutingManager#getHybridOptimization}, then replaces its depots
 * with {@link RoutingManager#addDepotsToHybridOptimization}. Set {@code HYBRID_DATE} to a day, as {@code YYYY-MM-DD},
 * with orders or contacts scheduled on a test account: the example replaces the depots of that day's optimization.
 */
public class HybridOptimization {

    public static void main(String[] args) {
        String date = System.getenv("HYBRID_DATE");
        if (date == null || date.isEmpty()) {
            System.err.println("Set HYBRID_DATE to a day (YYYY-MM-DD) with orders or contacts scheduled on a test"
                    + " account; this example replaces the depots of that day's hybrid optimization.");
            return;
        }
        try {
            LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            System.err.println("HYBRID_DATE must be a date as YYYY-MM-DD: " + date);
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            DataObject hybrid = manager.getHybridOptimization(date.trim(), -240);
            if (hybrid == null || hybrid.getOptimizationProblemId() == null) {
                System.out.println("No hybrid optimization for " + date + "; nothing to change.");
                return;
            }
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
