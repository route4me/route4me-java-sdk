package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.tracking.TrackingManager;

/**
 * Reads a route with its tracking history with {@link TrackingManager#getLastLocation}. Set {@code ROUTE_ID} to one
 * of your routes.
 */
public class TrackDeviceLastLocationHistory {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to one of your routes; this example prints its tracking history.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingManager manager = new TrackingManager(apiKey);
        try {
            DataObject obj = manager.getLastLocation(routeId);
            System.out.println(obj.getTrackingHistory());
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
