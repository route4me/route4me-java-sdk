package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Reads one stop of a route with {@link RoutingManager#getAddress}. Set {@code ROUTE_ID} to one of your routes and
 * {@code ROUTE_DESTINATION_ID} to one of its stops.
 */
public class GetAddress {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String stopId = System.getenv("ROUTE_DESTINATION_ID");
        if (routeId == null || routeId.isEmpty() || stopId == null || stopId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_DESTINATION_ID to one of your routes and one of its stops; this"
                    + " example reads the stop.");
            return;
        }
        Long routeDestinationId = ExampleInput.toLong("ROUTE_DESTINATION_ID", stopId);
        if (routeDestinationId == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            Address addr = manager.getAddress(routeId, routeDestinationId);
            System.out.println(addr);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
