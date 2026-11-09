package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Renames a route and assigns it a driver in one {@link RoutingManager#updateRoute} call. Set {@code ROUTE_ID} to a
 * route of a test account and {@code MEMBER_ID} to a driver of that account: the example renames the route and assigns
 * it to that driver.
 */
public class UpdateRouteNameDriver {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String memberId = System.getenv("MEMBER_ID");
        if (routeId == null || routeId.isEmpty() || memberId == null || memberId.isEmpty()) {
            System.err.println("Set ROUTE_ID to a route of a test account and MEMBER_ID to one of its drivers; this"
                    + " example renames the route and assigns it to the driver.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            // Create Route Object and Set the RouteID of the existing Route
            Route route = new Route();
            route.setId(routeId);
            // Create Parameters Object to Change Driver and Route Name
            Parameters params = new Parameters();
            params.setRouteName("Renaming and Assigning Driver to an Existing Route");
            params.setMemberId(memberId);
            // Set the parameters to the Route Object
            route.setParameters(params);
            // Update the Route
            route = routeManager.updateRoute(route);
            // Print The Updated Route
            System.out.println(route);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }
}
