package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.serdes.DataObjectDeserializer;

/**
 *
 * @author juan
 */
public class GetOptimization {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        OptimizationManager optimizationManager = route4me.getOptimizationManager();
        DataObject responseObject = optimizationManager.getOptimizations(10, 5);        
        String optimizationProblemID = responseObject.getOptimizations().get(0).getOptimization_problem_id();
        responseObject = optimizationManager.getOptimization(optimizationProblemID);        
        System.out.println("Optimization ID: " + responseObject.getOptimization_problem_id());
       
    }    
    
    
}
