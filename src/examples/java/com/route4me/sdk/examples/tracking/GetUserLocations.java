package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.tracking.TrackingManager;
import com.route4me.sdk.services.tracking.UserLocation;

/**
 * Prints the last known position of each user of the account with {@link TrackingManager#getUserLocations}; pass a
 * search text, such as an e-mail, to narrow the list.
 */
public class GetUserLocations {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingManager manager = new TrackingManager(apiKey);
        String query = args.length > 0 ? args[0] : null;
        try {
            UserLocation[] locations = manager.getUserLocations(query);
            if (locations != null) {
                for (UserLocation location : locations) {
                    if (location.getTracking() != null) {
                        System.out.println(location.getMemberData() + ": " + location.getTracking().getPositionLat()
                                + ", " + location.getTracking().getPositionLng());
                    }
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
