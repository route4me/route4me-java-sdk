package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RouteDeletedResponse;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class DeleteRoutes {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            List<Route> routes = routeManager.getRoutes(new RoutesRequest().setLimit(3).setOffset(10));
            if (routes.size() < 2) {
                throw new RuntimeException("Not enough routes");
            }
            RouteDeletedResponse deleted = routeManager.deleteRoutes(routes.get(0).getId(), routes.get(1).getId());
            System.out.println(deleted);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }
    }

}
