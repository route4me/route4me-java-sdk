package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.MovedAddressesResponse;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Moves a stop to another route with {@link RoutingManager#moveAddresses}. Set {@code ROUTE_DESTINATION_ID} to a stop
 * of a test account and {@code TO_ROUTE_ID} to the route that receives it: the example changes both routes.
 */
public class MoveAddressesToRoute {

    public static void main(String[] args) {
        String routeDestinationId = System.getenv("ROUTE_DESTINATION_ID");
        String toRouteId = System.getenv("TO_ROUTE_ID");
        if (routeDestinationId == null || routeDestinationId.isEmpty() || toRouteId == null || toRouteId.isEmpty()) {
            System.err.println("Set ROUTE_DESTINATION_ID and TO_ROUTE_ID to a stop and a route of a test account;"
                    + " this example moves the stop.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            System.out.println("Moving Destination " + routeDestinationId + " to Route " + toRouteId);
            MovedAddressesResponse response = manager.moveAddresses(Long.valueOf(routeDestinationId), toRouteId);
            System.out.println(response);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
