package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RouteUpdateParameters;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Re-sequences all the stops of a route with {@link RoutingManager#updateRoute(Route, RouteUpdateParameters)} and
 * {@code reoptimize}, which replaces the deprecated {@code RoutingManager.resequenceReoptimizeRoute}. Set
 * {@code ROUTE_ID} to a route of a test account: the example changes the order of its stops.
 */
public class ResequenceAllAddresses {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example re-sequences its"
                    + " stops.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            Route route = new Route();
            route.setId(routeId);
            Route resequenced = manager.updateRoute(route,
                    new RouteUpdateParameters().setReoptimize(true).setRemaining(false));
            System.out.println("Re-sequenced route: " + resequenced.getId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
