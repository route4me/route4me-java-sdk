package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RouteListRequest;
import com.route4me.sdk.services.routing.RoutingManagerV5;
import com.route4me.sdk.services.routing.search.RouteData;
import com.route4me.sdk.services.routing.search.RouteSearchResult;

import java.util.Arrays;
import java.util.Collections;

/**
 * Lists the routes scheduled in October 2026, newest first, through the V5 API with
 * {@link RoutingManagerV5#getRoutes}.
 */
public class GetRoutesV5 {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManagerV5 manager = new RoutingManagerV5(apiKey);
        RouteListRequest request = new RouteListRequest()
                .setFilters(Collections.<String, Object>singletonMap("schedule_date",
                        Arrays.asList("2026-10-01", "2026-10-31")))
                .setOrderBy(Collections.singletonList(Arrays.asList("route_created_unix", "desc")))
                .setTimezone("America/New_York")
                .setPage(1)
                .setPerPage(20);
        try {
            RouteSearchResult page = manager.getRoutes(request);
            if (page.getData() != null) {
                for (RouteData route : page.getData()) {
                    System.out.println(route.getRouteID() + ": " + route.getRouteName());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
