package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class UpdateRoute {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            List<Route> routes = routeManager.getRoutes(new RoutesRequest().setLimit(10));
            //fetches complete data
            Route r = routeManager.getRoute(new RoutesRequest().setId(routes.get(0).getId()));
            r.setVehicleAlias("Johnyy");
            Route newRoute = routeManager.updateRoute(r);
            System.out.println(newRoute);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }


}
