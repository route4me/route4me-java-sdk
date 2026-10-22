package com.route4me.sdk.examples.activities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.activities.ActivityManager;

/**
 * Posts a user message to the activity feed of a route with {@link ActivityManager#logActivity}.
 */
public class LogActivity {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        ActivityManager manager = new ActivityManager(apiKey);
        String routeId = "ROUTE_ID_HERE";
        try {
            manager.logActivity("The driver is running 10 minutes late", routeId);
            System.out.println("The message was logged on route " + routeId);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
