package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Removes one stop from a route with {@link RoutingManager#deleteAddress}. Set {@code ROUTE_ID} to a route of a test
 * account and {@code ROUTE_DESTINATION_ID} to one of its stops that is not a depot: the example deletes that stop.
 */
public class DeleteAddress {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String routeDestinationId = System.getenv("ROUTE_DESTINATION_ID");
        if (routeId == null || routeId.isEmpty() || routeDestinationId == null || routeDestinationId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_DESTINATION_ID to a route of a test account and one of its"
                    + " stops; this example deletes the stop.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            System.out.println("Deleting Address: " + routeDestinationId + " from Route: " + routeId);
            manager.deleteAddress(routeId, Long.valueOf(routeDestinationId));
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
