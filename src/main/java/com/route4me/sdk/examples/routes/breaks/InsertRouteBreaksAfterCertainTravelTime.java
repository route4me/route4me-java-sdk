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
 *
 * @author juan
 */
public class InsertRouteBreaksAfterCertainTravelTime {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteBreaksManager manager = new RouteBreaksManager(apiKey);
        try {
            Object response = manager.insertBreakAfterCertainTravelTime(900, 1800, true, "E206A767D62BBE4A179A82BB12A1C8D1", "C7330B977303A221ED75501F782F5928");
            System.out.println(response);
        } catch (APIException ex) {
            Logger.getLogger(InsertRouteBreaksAfterCertainTravelTime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
