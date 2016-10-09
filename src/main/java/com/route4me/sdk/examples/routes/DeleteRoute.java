package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class DeleteRoute {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            List<Route> routes = routeManager.getRoutes(new RoutesRequest().setLimit(3).setOffset(10));
            List<Route> deleted = routeManager.deleteRoutes(routes.get(0).getId());
            System.out.println(deleted);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }
    }

}
