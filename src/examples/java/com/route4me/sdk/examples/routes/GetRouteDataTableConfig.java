package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RouteDataTableConfigResponse;
import com.route4me.sdk.services.routing.RoutingManagerV5;

/**
 * Prints the fields the route data table can sort and filter by, with
 * {@link RoutingManagerV5#getRouteDataTableConfig}.
 */
public class GetRouteDataTableConfig {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManagerV5 manager = new RoutingManagerV5(apiKey);
        try {
            RouteDataTableConfigResponse config = manager.getRouteDataTableConfig();
            if (config.getApiCapabilities() != null) {
                System.out.println("Sortable: " + config.getApiCapabilities().getSortableFields());
                System.out.println("Filterable: " + config.getApiCapabilities().getFilterableFields());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
