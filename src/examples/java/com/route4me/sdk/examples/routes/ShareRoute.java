package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.StatusResponse;

/** Sends a route to an e-mail address with {@link RoutingManager#routeSharing}. */
public class ShareRoute {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            StatusResponse shared = manager.routeSharing("ROUTE_ID_HERE", "dispatcher@example.com");
            System.out.println("Shared: " + shared.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
