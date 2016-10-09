package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class ReOptimization {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<DataObject> data = manager.getOptimizations(10, 5);
            DataObject obj = data.get(0);
            OptimizationParameters parameters = new OptimizationParameters();
            parameters.setProblemId(obj.getOptimizationProblemId());
            parameters.setReoptimize(true);
            DataObject updated = manager.updateOptimization(parameters);
            System.out.println(updated);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }


}
