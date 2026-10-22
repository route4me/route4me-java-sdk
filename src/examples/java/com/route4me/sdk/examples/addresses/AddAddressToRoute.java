package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * Finds a route ({@link RoutingManager#getRoutes}, {@link RoutingManager#getRoute}) and adds an address to it with
 * {@link RoutingManager#addAddressesToRoute}.
 */
public class AddAddressToRoute {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<Route> routes = manager.getRoutes(new RoutesRequest().setLimit(10));
            Route route = manager.getRoute(new RoutesRequest().setId(routes.get(0).getId()));
            DataObject obj = manager.addAddressesToRoute(routes.get(1).getId(), route.getAddresses());
            System.out.println(obj);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
