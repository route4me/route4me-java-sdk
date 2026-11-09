package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Re-optimizes an optimization with {@link RoutingManager#reOptimizationByOptimizationID}. Set
 * {@code OPTIMIZATION_PROBLEM_ID} to an optimization of a test account: the example re-optimizes it, which changes its
 * routes.
 */
public class ReOptimization {

    public static void main(String[] args) {
        String optimizationProblemId = System.getenv("OPTIMIZATION_PROBLEM_ID");
        if (optimizationProblemId == null || optimizationProblemId.isEmpty()) {
            System.err.println("Set OPTIMIZATION_PROBLEM_ID to the ID of an optimization of a test account; this"
                    + " example re-optimizes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            OptimizationParameters parameters = new OptimizationParameters();
            parameters.setProblemId(optimizationProblemId);
            parameters.setReoptimize(true);
            DataObject updated = manager.reOptimizationByOptimizationID(parameters);
            System.out.println(updated);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ReOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
