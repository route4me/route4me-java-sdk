package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Copies a route with {@link RoutingManager#duplicateRoute}. Set {@code ROUTE_ID} to a route of a test account: the
 * example adds a copy of it to the account.
 */
public class DuplicateRoute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example copies it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            RoutingManager.DuplicateRouteResponse resp = routeManager.duplicateRoute(routeId);
            System.out.println(resp);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }
    }

}
