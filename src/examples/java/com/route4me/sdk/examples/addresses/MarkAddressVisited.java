package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Marks a stop of a route as visited, now and here, with {@link RoutingManager#markAddressVisited}. Set
 * {@code ROUTE_ID} and {@code ROUTE_DESTINATION_ID} to a route of a test account and one of its stops: the example
 * changes the stop.
 */
public class MarkAddressVisited {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String routeDestinationId = System.getenv("ROUTE_DESTINATION_ID");
        if (routeId == null || routeId.isEmpty() || routeDestinationId == null || routeDestinationId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_DESTINATION_ID to a route of a test account and one of its"
                    + " stops; this example marks the stop as visited.");
            return;
        }
        Long stopId = ExampleInput.toLong("ROUTE_DESTINATION_ID", routeDestinationId);
        if (stopId == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            long now = System.currentTimeMillis() / 1000L;
            Integer marked = manager.markAddressVisited(routeId, stopId, true, null, now,
                    38.141598, -85.793846);
            System.out.println("Marked as visited: " + marked);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
