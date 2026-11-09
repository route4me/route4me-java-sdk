package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.StatusResponse;

/**
 * Sends a route to an e-mail address with {@link RoutingManager#routeSharing}. Set {@code ROUTE_ID} to a route of a
 * test account: the example shares it.
 */
public class ShareRoute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example shares it by"
                    + " e-mail.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            StatusResponse shared = manager.routeSharing(routeId, "dispatcher@example.com");
            System.out.println("Shared: " + shared.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
