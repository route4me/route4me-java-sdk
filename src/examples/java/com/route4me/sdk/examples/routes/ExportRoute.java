package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RouteExportRequest;
import com.route4me.sdk.services.routing.RoutingManagerV5;

/**
 * Exports a route with its addresses as CSV with {@link RoutingManagerV5#exportRoute}. Set {@code ROUTE_ID} to one
 * of your routes.
 */
public class ExportRoute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example exports it as CSV.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManagerV5 manager = new RoutingManagerV5(apiKey);
        RouteExportRequest request = new RouteExportRequest()
                .setFormat("csv")
                .setWithAddresses(true)
                .setAllCustomFields(true);
        try {
            String csv = manager.exportRoute(routeId, request);
            System.out.println(csv);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
