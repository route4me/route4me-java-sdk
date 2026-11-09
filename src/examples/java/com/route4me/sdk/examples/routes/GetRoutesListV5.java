package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RouteListRequest;
import com.route4me.sdk.services.routing.RoutingManagerV5;
import com.route4me.sdk.services.routing.search.RouteData;
import com.route4me.sdk.services.routing.search.RouteSearchResult;

import java.util.Arrays;

/**
 * Searches the routes by name, returning only a few fields, through the V5 API with
 * {@link RoutingManagerV5#getRoutesList}.
 */
public class GetRoutesListV5 {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManagerV5 manager = new RoutingManagerV5(apiKey);
        RouteListRequest request = new RouteListRequest()
                .setQuery("Morning")
                .setFields(Arrays.asList("route_id", "route_name", "destination_count"))
                .setPage(1)
                .setPerPage(50);
        try {
            RouteSearchResult page = manager.getRoutesList(request);
            if (page.getData() != null) {
                for (RouteData route : page.getData()) {
                    System.out.println(route.getRouteName() + ": " + route.getDestinationCount() + " stops");
                }
            }
            if (page.getMeta() != null) {
                System.out.println("Total: " + page.getMeta().getTotal());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
