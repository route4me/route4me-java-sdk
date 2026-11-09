package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Reads an optimization with its routes with {@link RoutingManager#getOptimization}. Set
 * {@code OPTIMIZATION_PROBLEM_ID} to one of your optimizations.
 */
public class GetOptimization {

    public static void main(String[] args) {
        String optimizationProblemId = System.getenv("OPTIMIZATION_PROBLEM_ID");
        if (optimizationProblemId == null || optimizationProblemId.isEmpty()) {
            System.err.println("Set OPTIMIZATION_PROBLEM_ID to the ID of one of your optimizations; this example reads"
                    + " it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            DataObject optimization = manager.getOptimization(new OptimizationParameters()
                    .setProblemId(optimizationProblemId));
            System.out.println(optimization);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
