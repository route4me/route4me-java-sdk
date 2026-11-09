package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Gets a route with the compressed path points between its stops, with {@link RoutingManager#getRoute} and
 * {@code route_path_output=Points}. Set {@code ROUTE_ID} to one of your routes.
 */
public class GetRoutePathPoints {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example reads its path points.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            Route route = manager.getRoute(new RoutesRequest().setId(routeId).setRoutePathOutput("Points")
                    .setCompressPathPoints(true));
            if (route.getAddresses() != null) {
                for (Address address : route.getAddresses()) {
                    System.out.println(address.getRouteDestinationId() + ": " + address.getAddress());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
