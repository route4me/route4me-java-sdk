package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.MovedAddressesResponse;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Moves a stop to another route, after a given stop of that route, with {@link RoutingManager#moveAddresses}. Set
 * {@code ROUTE_DESTINATION_ID} to a stop of a test account, {@code TO_ROUTE_ID} to the route that receives it and
 * {@code AFTER_ROUTE_DESTINATION_ID} to the stop of that route it goes after: the example changes both routes.
 */
public class MoveAddressesToRouteAfterDestination {

    public static void main(String[] args) {
        String routeDestinationId = System.getenv("ROUTE_DESTINATION_ID");
        String toRouteId = System.getenv("TO_ROUTE_ID");
        String afterRouteDestinationId = System.getenv("AFTER_ROUTE_DESTINATION_ID");
        if (routeDestinationId == null || routeDestinationId.isEmpty() || toRouteId == null || toRouteId.isEmpty()
                || afterRouteDestinationId == null || afterRouteDestinationId.isEmpty()) {
            System.err.println("Set ROUTE_DESTINATION_ID, TO_ROUTE_ID and AFTER_ROUTE_DESTINATION_ID to a stop, a route"
                    + " and a stop of that route of a test account; this example moves the first stop.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            System.out.println("Moving Destination " + routeDestinationId + " to Route " + toRouteId
                    + " after Destination " + afterRouteDestinationId);
            MovedAddressesResponse response = manager.moveAddresses(Long.valueOf(routeDestinationId), toRouteId,
                    Long.valueOf(afterRouteDestinationId));
            System.out.println(response);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
