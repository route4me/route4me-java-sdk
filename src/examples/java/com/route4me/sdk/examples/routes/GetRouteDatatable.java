package com.route4me.sdk.examples.routes;

import com.google.gson.JsonElement;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RouteListRequest;
import com.route4me.sdk.services.routing.RoutingManagerV5;

import java.util.Arrays;
import java.util.Collections;

/**
 * Reads the first page of the route data table for a week with
 * {@link RoutingManagerV5#getRouteDatatableWithElasticSearch}.
 */
public class GetRouteDatatable {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManagerV5 manager = new RoutingManagerV5(apiKey);
        RouteListRequest request = new RouteListRequest()
                .setFilters(Collections.<String, Object>singletonMap("schedule_date",
                        Arrays.asList("2026-10-19", "2026-10-25")))
                .setPage(1)
                .setPerPage(20);
        try {
            JsonElement table = manager.getRouteDatatableWithElasticSearch(request);
            System.out.println(table);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
