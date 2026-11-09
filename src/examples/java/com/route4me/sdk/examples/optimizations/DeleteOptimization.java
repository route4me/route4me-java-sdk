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
 * Deletes an optimization with {@link RoutingManager#deleteOptimization}. Set {@code OPTIMIZATION_PROBLEM_ID} to an
 * optimization of a test account: the example deletes it.
 */
public class DeleteOptimization {
    public static void main(String[] args) {
        String optimizationProblemId = System.getenv("OPTIMIZATION_PROBLEM_ID");
        if (optimizationProblemId == null || optimizationProblemId.isEmpty()) {
            System.err.println("Set OPTIMIZATION_PROBLEM_ID to the ID of an optimization of a test account; this"
                    + " example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager routeManager = new RoutingManager(apiKey);
        ArrayList<String> optimizationProblemIDs = new ArrayList<String>();
        optimizationProblemIDs.add(optimizationProblemId);
        try {
            Map<String, Object> response = routeManager.deleteOptimization(optimizationProblemIDs);
            System.out.println(response);
        } catch (APIException ex) {
            Logger.getLogger(DeleteOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
