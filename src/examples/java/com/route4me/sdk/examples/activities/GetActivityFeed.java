package com.route4me.sdk.examples.activities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.activities.Activities;
import com.route4me.sdk.services.activities.Activity;
import com.route4me.sdk.services.activities.ActivityManager;
import com.route4me.sdk.services.activities.ActivityRequest;
import com.route4me.sdk.services.activities.ActivityType;

/**
 * Reads the destinations inserted into a route from the V4 activity feed with
 * {@link ActivityManager#getActivityFeed}. Set {@code ROUTE_ID} to one of your routes.
 */
public class GetActivityFeed {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example reads its activity feed.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        ActivityManager manager = new ActivityManager(apiKey);
        try {
            Activities feed = manager.getActivityFeed(new ActivityRequest().setRouteId(routeId)
                    .setType(ActivityType.INSERT_DESTINATION).setLimit(10));
            System.out.println("Activities: " + feed.getTotal());
            if (feed.getResults() != null) {
                for (Activity activity : feed.getResults()) {
                    System.out.println(activity.getTimestamp() + " " + activity.getMessage());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
