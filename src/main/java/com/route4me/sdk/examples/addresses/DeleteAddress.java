package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

public class DeleteAddress {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            Address addr = null;
            List<Route> routes = manager.getRoutes(new RoutesRequest().setLimit(64));
            for (Route r : routes) {
                Route full = manager.getRoute(new RoutesRequest().setId(r.getId()));
                if (full.getAddresses().isEmpty()) {
                    continue;
                }
                for (Address add : full.getAddresses()) {
                    if (!add.isDepot()) {
                        addr = add;
                        return;
                    }
                }
                if (addr == null) {
                    throw new RuntimeException("Cannot find an address for delete operation");
                }
            }
            manager.deleteAddress(addr.getOptimizationProblemId(), addr.getRouteDestinationId());
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
