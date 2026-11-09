package com.route4me.sdk.examples.activities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.activities.Activities;
import com.route4me.sdk.services.activities.Activity;
import com.route4me.sdk.services.activities.ActivityManager;
import com.route4me.sdk.services.activities.ActivityRequest;

/**
 * Lists the activities of one route with {@link ActivityManager#getActivities}. Set {@code ROUTE_ID} to one of your
 * routes.
 */
public class GetActivities {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example lists its activities.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        ActivityManager manager = new ActivityManager(apiKey);
        try {
            System.out.println("Getting Activities for Route ID: " + routeId);
            Activities responseObject = manager.getActivities(new ActivityRequest().setLimit(10).setRouteId(routeId));
            System.out.println("Total Activities: " + responseObject.getTotal());
            if (responseObject.getResults() != null) {
                for (Activity activity : responseObject.getResults()) {
                    System.out.println(activity);
                }
            }
        } catch (APIException e) {
            //Handle error
            e.printStackTrace();
        }
    }
}
