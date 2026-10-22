package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author route4me
 */
public class ReOptimization {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<DataObject> data = manager.getOptimizations(1, 0);
            DataObject obj = data.get(0);
            OptimizationParameters parameters = new OptimizationParameters();
            parameters.setProblemId(obj.getOptimizationProblemId());
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
