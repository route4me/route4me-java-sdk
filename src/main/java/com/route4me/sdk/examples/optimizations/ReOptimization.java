package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.enums.Constants;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author juan
 */
public class ReOptimization {
    
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        Map<String, String> params = new HashMap<>();
        OptimizationManager optimizationManager = route4me.getOptimizationManager();
        DataObject responseObject = optimizationManager.getOptimizations(10, 5);        
        String optimizationProblemID = responseObject.getOptimizations().get(0).getOptimization_problem_id();
        params.put("optimization_problem_id", optimizationProblemID);
        params.put("reoptimize", "1");
        params.put("remote_ip", "192168001001");
        optimizationManager = route4me.getOptimizationManager();
        optimizationManager.setParams(params);
        responseObject = optimizationManager.reOptimization();
        System.out.println("Optimization Problem ID:" + responseObject.getOptimization_problem_id());
        System.out.println("State:" + Constants.OptimizationState.get(responseObject.getState().intValue()));
        
    }    
    
    
}
