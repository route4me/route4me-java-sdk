package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RouteExportRequest;
import com.route4me.sdk.services.routing.RoutingManagerV5;

/** Exports a route with its addresses as CSV with {@link RoutingManagerV5#exportRoute}. */
public class ExportRoute {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManagerV5 manager = new RoutingManagerV5(apiKey);
        RouteExportRequest request = new RouteExportRequest()
                .setFormat("csv")
                .setWithAddresses(true)
                .setAllCustomFields(true);
        try {
            String csv = manager.exportRoute("ROUTE_ID_HERE", request);
            System.out.println(csv);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
