/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 *
 * @author juan
 */
public class AssignVehicle {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            Route route = routeManager.getRoute(new RoutesRequest().setId("")); //SET VALID ROUTE ID
            String vehicleId = ""; //SET VALID VEHICLE ID
            Route newRoute = routeManager.assignVehicle(route, vehicleId);
            System.out.println(newRoute);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }

}
