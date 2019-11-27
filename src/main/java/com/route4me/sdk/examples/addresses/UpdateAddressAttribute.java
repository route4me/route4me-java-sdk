package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

public class UpdateAddressAttribute {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<Route> routes = manager.getRoutes(new RoutesRequest().setLimit(10));
            Route route = manager.getRoute(new RoutesRequest().setId(routes.get(0).getId()));
            Address addr = manager.getAddress(route.getId(), route.getAddresses().get(0).getRouteDestinationId());
            addr.setAlias("DEPOT");
            Address address = manager.updateAddressAttribute(route.getId(), route.getAddresses().get(0).getRouteDestinationId(), addr);
            System.out.println(address);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
