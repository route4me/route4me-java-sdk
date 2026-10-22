package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RouteUpdateParameters;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Re-optimizes the stops of a route that are not visited yet, then recomputes the directions of another route,
 * with {@link RoutingManager#updateRoute(Route, RouteUpdateParameters)}.
 */
public class ReoptimizeRoute {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            Route route = new Route();
            route.setId("ROUTE_ID_HERE");
            Route reoptimized = manager.updateRoute(route,
                    new RouteUpdateParameters().setReoptimize(true).setRemaining(true));
            System.out.println("Re-optimized route: " + reoptimized.getId());

            Route other = new Route();
            other.setId("OTHER_ROUTE_ID_HERE");
            Route recomputed = manager.updateRoute(other, new RouteUpdateParameters().setRecomputeDirections(true));
            System.out.println("Recomputed the directions of route: " + recomputed.getId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
