package com.route4me.sdk.examples.activities;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.activities.Activities;
import com.route4me.sdk.services.activities.Activity;
import com.route4me.sdk.services.activities.ActivityManager;
import com.route4me.sdk.services.activities.ActivityRequest;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

public class GetActivities {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        ActivityManager manager = new ActivityManager(apiKey);
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            List<Route> routes = routeManager.getRoutes(new RoutesRequest().setLimit(10));
            String routeID = routes.get(2).getId();
            System.out.println("Getting Activities for Route ID: " + routeID);
            Activities responseObject = manager.getActivities(new ActivityRequest().setLimit(10).setRouteId(routeID));
            System.out.println("Total Activities: " + responseObject.getTotal());
            for (Activity activity : responseObject.getResults()) {
                System.out.println(activity);
            }
        } catch (APIException e) {
            //Handle error
            e.printStackTrace();
        }
    }
}
