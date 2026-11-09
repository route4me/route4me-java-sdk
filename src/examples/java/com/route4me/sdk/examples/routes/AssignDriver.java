/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Assigns a driver to a route with {@link RoutingManager#assignDriver}. Set {@code ROUTE_ID} to a route of a test
 * account and {@code MEMBER_ID} to one of its drivers: the example assigns the route to the driver.
 */
public class AssignDriver {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String driverId = System.getenv("MEMBER_ID");
        if (routeId == null || routeId.isEmpty() || driverId == null || driverId.isEmpty()) {
            System.err.println("Set ROUTE_ID to a route of a test account and MEMBER_ID to one of its drivers; this"
                    + " example assigns the route to the driver.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            Route newRoute = routeManager.assignDriver(routeId, driverId);
            System.out.println(newRoute);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }

}
