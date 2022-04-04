package com.route4me.sdk.examples.optimizations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class GetOptimizations {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<DataObject> data = manager.getOptimizations(10, 5);
            for (DataObject optimization : data) {
                System.out.println(optimization);
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }


}
