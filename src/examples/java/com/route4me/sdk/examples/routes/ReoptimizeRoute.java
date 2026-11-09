package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RouteUpdateParameters;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Re-optimizes the stops of a route that are not visited yet, then recomputes the directions of another route,
 * with {@link RoutingManager#updateRoute(Route, RouteUpdateParameters)}. Set {@code ROUTE_ID} and
 * {@code ROUTE_ID_2} to two routes of a test account: the example re-optimizes the first one and recomputes the
 * directions of the second one.
 */
public class ReoptimizeRoute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String secondRouteId = System.getenv("ROUTE_ID_2");
        if (routeId == null || routeId.isEmpty() || secondRouteId == null || secondRouteId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_ID_2 to two routes of a test account; this example"
                    + " re-optimizes the first one and recomputes the directions of the second one.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            Route route = new Route();
            route.setId(routeId);
            Route reoptimized = manager.updateRoute(route,
                    new RouteUpdateParameters().setReoptimize(true).setRemaining(true));
            System.out.println("Re-optimized route: " + reoptimized.getId());

            Route other = new Route();
            other.setId(secondRouteId);
            Route recomputed = manager.updateRoute(other, new RouteUpdateParameters().setRecomputeDirections(true));
            System.out.println("Recomputed the directions of route: " + recomputed.getId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
