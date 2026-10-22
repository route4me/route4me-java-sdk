package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.StatusResponse;

/**
 * Marks a visited stop of a route as departed with {@link RoutingManager#markAddressDeparted}. The stop must be
 * marked visited first (see {@link MarkAddressVisited}).
 */
public class MarkAddressDeparted {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            StatusResponse departed = manager.markAddressDeparted("ROUTE_ID_HERE", 123456789L, true, null, null,
                    null, null);
            System.out.println("Marked as departed: " + departed.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
