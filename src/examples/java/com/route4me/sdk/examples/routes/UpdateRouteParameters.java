package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Changes a parameter of a route, its name, with {@link RoutingManager#updateRoute}. Set {@code ROUTE_ID} to a route of
 * a test account: the example changes it.
 */
public class UpdateRouteParameters {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example changes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            Parameters params = new Parameters();
            params.setRouteName("Example route, renamed");
            Route change = new Route();
            change.setId(routeId);
            change.setParameters(params);
            Route newRoute = routeManager.updateRoute(change);
            System.out.println(newRoute);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }

}
