package com.route4me.sdk.examples.activities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.activities.ActivityManager;

/**
 * Posts a user message to the activity feed of a route with {@link ActivityManager#logActivity}. Set
 * {@code ROUTE_ID} to a route of a test account: the example adds the message to its feed.
 */
public class LogActivity {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example posts a message to"
                    + " its activity feed.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        ActivityManager manager = new ActivityManager(apiKey);
        try {
            manager.logActivity("The driver is running 10 minutes late", routeId);
            System.out.println("The message was logged on route " + routeId);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
