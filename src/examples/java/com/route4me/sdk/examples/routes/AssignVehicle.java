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
 * Assigns a vehicle to a route with {@link RoutingManager#assignVehicle}. Set {@code ROUTE_ID} and
 * {@code VEHICLE_ID} to a route and a vehicle of a test account: the example assigns the vehicle to the route.
 */
public class AssignVehicle {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String vehicleId = System.getenv("VEHICLE_ID");
        if (routeId == null || routeId.isEmpty() || vehicleId == null || vehicleId.isEmpty()) {
            System.err.println("Set ROUTE_ID and VEHICLE_ID to a route and a vehicle of a test account; this example"
                    + " assigns the vehicle to the route.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            Route newRoute = routeManager.assignVehicle(routeId, vehicleId);
            System.out.println(newRoute);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }

}
