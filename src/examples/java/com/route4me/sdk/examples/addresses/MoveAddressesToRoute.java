package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.MovedAddressesResponse;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * Moves a destination from one route to another with {@link RoutingManager#moveAddresses}, after finding both
 * routes with {@link RoutingManager#getRoutes} and {@link RoutingManager#getRoute}.
 */
public class MoveAddressesToRoute {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<Route> routes = manager.getRoutes(new RoutesRequest().setLimit(2));
            Route fromRoute = manager.getRoute(new RoutesRequest().setId(routes.get(0).getId()));
            Route toRoute = manager.getRoute(new RoutesRequest().setId(routes.get(1).getId()));
            Number routeDestinationId = fromRoute.getAddresses().get(3).getRouteDestinationId();
            System.out.println("Moving Destination " + routeDestinationId + " from Route " + fromRoute.getId()
                    + " to Route " + toRoute.getId());
            MovedAddressesResponse response = manager.moveAddresses(routeDestinationId, toRoute.getId());
            System.out.println(response);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
