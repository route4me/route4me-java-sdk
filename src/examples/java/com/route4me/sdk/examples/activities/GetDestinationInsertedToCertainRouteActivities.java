// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.activities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.activities.Activities;
import com.route4me.sdk.services.activities.ActivityManager;
import com.route4me.sdk.services.activities.ActivityRequest;
import com.route4me.sdk.services.activities.ActivityType;

/**
 * Lists the destinations added to one route ({@code ActivityType.INSERT_DESTINATION}) with {@link
 * ActivityManager#getActivities}. Set {@code ROUTE_ID} to the ID of a route of your account.
 */
public class GetDestinationInsertedToCertainRouteActivities {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of your account.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        ActivityManager manager = new ActivityManager(apiKey);
        try {
            Activities activities = manager.getActivities(new ActivityRequest().setType(ActivityType.INSERT_DESTINATION)
                    .setRouteId(routeId));
            System.out.println(activities.getResults());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
// codebeat:enable[SIMILARITY]
