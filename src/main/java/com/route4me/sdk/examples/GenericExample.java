package com.route4me.sdk.examples;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.List;

/**
 * @author juan
 */
public class GenericExample {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<DataObject> responseObject = manager.getOptimizations(10, 5);
            DataObject opt = responseObject.get(0);
            System.out.println("Optimization ID: " + opt.getOptimizationProblemId());
            for (Address address : opt.getAddresses()) {
                System.out.println(address);
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }

    }
    //String jsonResponse = Manager.GSONSerializer.toJson(responseObject);

}
