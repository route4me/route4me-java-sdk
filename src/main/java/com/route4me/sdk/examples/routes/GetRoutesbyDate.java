package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;
import java.util.ArrayList;

import java.util.List;

public class GetRoutesbyDate {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        List<Route> routes = new ArrayList<>();
        RoutesRequest routeRequest  = new RoutesRequest();
        routeRequest.setStartDate("2022-04-05");
        routeRequest.setEndDate("2022-04-05");
        routeRequest.setLimit(2);
        
        try {
            routes = routeManager.getRoutes(routeRequest);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }
        int i = 0;
        for (Route r : routes) {
                i++;
                System.out.println("Route #" + i + " " + r.getId());
        }


    }
}
