package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RouteDeletedResponse;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Deletes two routes in one request with {@link RoutingManager#deleteRoutes}. Set {@code ROUTE_ID} and
 * {@code ROUTE_ID_2} to two routes of a test account: the example deletes them.
 */
public class DeleteRoutes {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String secondRouteId = System.getenv("ROUTE_ID_2");
        if (routeId == null || routeId.isEmpty() || secondRouteId == null || secondRouteId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_ID_2 to two routes of a test account;"
                    + " this example deletes them.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            RouteDeletedResponse deleted = routeManager.deleteRoutes(routeId, secondRouteId);
            System.out.println(deleted);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }
    }

}
