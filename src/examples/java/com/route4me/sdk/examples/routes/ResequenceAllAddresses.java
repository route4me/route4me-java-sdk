package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RouteUpdateParameters;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Re-sequences all the stops of a route with {@link RoutingManager#updateRoute(Route, RouteUpdateParameters)} and
 * {@code reoptimize}, which replaces the deprecated {@code RoutingManager.resequenceReoptimizeRoute}.
 */
public class ResequenceAllAddresses {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            Route route = new Route();
            route.setId("ROUTE_ID_HERE");
            Route resequenced = manager.updateRoute(route,
                    new RouteUpdateParameters().setReoptimize(true).setRemaining(false));
            System.out.println("Re-sequenced route: " + resequenced.getId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
