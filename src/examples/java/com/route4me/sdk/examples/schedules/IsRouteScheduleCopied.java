package com.route4me.sdk.examples.schedules;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.schedules.SchedulesManager;
import com.route4me.sdk.services.schedules.StatusResponse;

/**
 * Tells whether a route was created by a schedule, with {@link SchedulesManager#isRouteScheduleCopied}. Set
 * {@code ROUTE_ID} to a route of the account.
 */
public class IsRouteScheduleCopied {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of the account.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        SchedulesManager manager = new SchedulesManager(apiKey);
        try {
            StatusResponse copied = manager.isRouteScheduleCopied(routeId);
            System.out.println("Created by a schedule: " + copied.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
