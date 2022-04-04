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
 *
 * @author juan
 */
public class AssignDriver {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            String routeId = "FA249A8FAC4D7FA7938C77784737481F";
            String driverId = "1";
            Route newRoute = routeManager.assignDriver(routeId, driverId);
            System.out.println(newRoute);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }

}
