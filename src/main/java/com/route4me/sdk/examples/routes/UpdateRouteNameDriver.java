package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutingManager;


/**
 * @author Route4Me
 */
public class UpdateRouteNameDriver {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            // Create Route Object and Set the RouteID of the existing Route
            Route route = new Route();
            route.setId("11111111111111111111111111111111");
            // Create Parameters Object to Change Driver and Route Name
            Parameters params = new Parameters();
            params.setRouteName("Renaming and Assining Driver to an Existing Route");
            params.setMemberId("1");
            // Set the parameters to the Route Object
            route.setParameters(params);
            // Update the Route
            route = routeManager.updateRoute(route);
            // Print The Updated Route
            System.out.println(route);
        } catch (APIException e) {
            //handle exceptions
            System.err.println(e);
        }

    }
}
