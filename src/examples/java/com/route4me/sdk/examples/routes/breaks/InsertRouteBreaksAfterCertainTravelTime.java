/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.routes.breaks;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.breaks.RouteBreaksManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adds a break after a given travel time to routes with
 * {@link RouteBreaksManager#insertBreakAfterCertainTravelTime}. Set {@code ROUTE_IDS}
 * to the comma-separated IDs of routes of a test account: the example adds the break to them.
 */
public class InsertRouteBreaksAfterCertainTravelTime {

    public static void main(String[] args) {
        String routeIds = System.getenv("ROUTE_IDS");
        if (routeIds == null || routeIds.trim().isEmpty()) {
            System.err.println("Set ROUTE_IDS to the comma-separated IDs of routes of a test account; this example"
                    + " adds a break to them.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteBreaksManager manager = new RouteBreaksManager(apiKey);
        try {
            Object response = manager.insertBreakAfterCertainTravelTime(900, 1800, true,
                    routeIds.trim().split("\\s*,\\s*"));
            System.out.println(response);
        } catch (APIException ex) {
            Logger.getLogger(InsertRouteBreaksAfterCertainTravelTime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
