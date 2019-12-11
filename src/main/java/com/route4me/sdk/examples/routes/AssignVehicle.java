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
public class AssignVehicle {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            String routeId = "FA249A8FAC4D7FA7938C77784737481F";
            String vehicleId = "64ACF1E576D078D853F935E788A42F93";
            Route newRoute = routeManager.assignVehicle(routeId, vehicleId);
            System.out.println(newRoute);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }

}
