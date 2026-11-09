package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Reads a route with its addresses and notes with {@link RoutingManager#getRoute}. Set {@code ROUTE_ID} to one of
 * your routes.
 */
public class GetRoute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example reads it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            //fetches complete data
            Route r = routeManager.getRoute(new RoutesRequest().setNotes(Boolean.TRUE).setId(routeId));
            System.out.println(r);
            if (r != null && r.getAddresses() != null) {
                for (Address address : r.getAddresses()) {
                    System.out.println("Address: " + address.getAddress() + " Running Distance: "
                            + (address.getManifest() == null ? null : address.getManifest().get("running_distance")));
                }
            }
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }
    }

}
