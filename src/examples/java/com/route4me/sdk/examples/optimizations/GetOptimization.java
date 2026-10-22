package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class GetOptimization {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<DataObject> data = manager.getOptimizations(10, 5);
            String optimizationProblemID = data.get(0).getOptimizationProblemId();
            DataObject optimization = manager.getOptimization(new OptimizationParameters().setProblemId(optimizationProblemID));
            System.out.println(optimization);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }


}
