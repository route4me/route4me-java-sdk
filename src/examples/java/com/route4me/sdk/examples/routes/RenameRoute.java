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
 * Renames a route with {@link RoutingManager#renameRoute}. Set {@code ROUTE_ID} to a route of a test account: the
 * example renames it.
 */
public class RenameRoute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example renames it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        try {
            RouteRenamedStatus status = routeManager.renameRoute("Renamed Route", routeId);
            //fetches complete data
            System.out.println(status);
        } catch (APIException e) {
            //handle exceptions
            e.printStackTrace();
        }

    }

}
