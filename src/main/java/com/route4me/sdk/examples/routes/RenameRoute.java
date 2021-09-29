/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.routes;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RouteRenamedStatus;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 *
 * @author Route4Me
 */
public class RenameRoute {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            RouteRenamedStatus status = routeManager.renameRoute("Renamed Route", "064E14375541A4ECADCF9B3DDAB6958D");
            //fetches complete data
            System.out.println(status);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }
    
}
