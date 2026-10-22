package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Gets a route with the compressed path points between its stops, with {@link RoutingManager#getRoute} and
 * {@code route_path_output=Points}.
 */
public class GetRoutePathPoints {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            Route route = manager.getRoute(new RoutesRequest().setId("ROUTE_ID_HERE").setRoutePathOutput("Points")
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
