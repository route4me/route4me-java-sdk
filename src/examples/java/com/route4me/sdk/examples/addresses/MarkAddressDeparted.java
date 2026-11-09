package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.StatusResponse;

/**
 * Marks a visited stop of a route as departed with {@link RoutingManager#markAddressDeparted}. The stop must be
 * marked visited first (see {@link MarkAddressVisited}). Set {@code ROUTE_ID} and {@code ROUTE_DESTINATION_ID} to a
 * route of a test account and one of its stops: the example changes the stop.
 */
public class MarkAddressDeparted {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String routeDestinationId = System.getenv("ROUTE_DESTINATION_ID");
        if (routeId == null || routeId.isEmpty() || routeDestinationId == null || routeDestinationId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_DESTINATION_ID to a route of a test account and one of its"
                    + " stops; this example marks the stop as departed.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            StatusResponse departed = manager.markAddressDeparted(routeId, Long.valueOf(routeDestinationId), true,
                    null, null, null, null);
            System.out.println("Marked as departed: " + departed.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
