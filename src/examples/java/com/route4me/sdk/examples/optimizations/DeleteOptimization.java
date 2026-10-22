/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class DeleteOptimization {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        ArrayList<String> optimizationProblemIDs = new ArrayList<String>();
        optimizationProblemIDs.add("07C248AE57754B3EB91D1C934AD35B2D");
        try {
            Map<String, Object> response = routeManager.deleteOptimization(optimizationProblemIDs);
            System.out.println(response);
        } catch (APIException ex) {
            Logger.getLogger(DeleteOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
