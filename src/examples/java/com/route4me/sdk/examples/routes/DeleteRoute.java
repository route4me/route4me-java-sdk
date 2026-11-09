package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RouteDeletedResponse;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Deletes one route with {@link RoutingManager#deleteRoutes}. Set {@code ROUTE_ID} to a route of a test account: the
 * example deletes it.
 */
public class DeleteRoute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            RouteDeletedResponse deleted = routeManager.deleteRoutes(routeId);
            System.out.println(deleted);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }
    }

}
