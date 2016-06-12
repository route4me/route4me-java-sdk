
package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;

/**
 *
 * @author juan
 */
public class GenericExample {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        OptimizationManager optimizationManager = route4me.getOptimizationManager();
        DataObject responseObject = optimizationManager.getOptimizations(10, 5);
        String optimizationID = responseObject.getOptimizations().get(0).getOptimizationProblemId();
        DataObject optimization;
        optimization = optimizationManager.getOptimization(optimizationID);
        System.out.println("Optimization ID: " + optimization.getOptimizationProblemId());
        int i = 0;
        for (Address address : optimization.getAddresses()) {
            i = i + 1;
            System.out.println("Address " + i);
            System.out.println("\tAddress: " + address.getAddress());
            System.out.println("\tLatitude: " + address.getLatitude());
            System.out.println("\tLongitude: " + address.getLongitude());
            System.out.println();
        }

    }
    //String jsonResponse = Manager.GSONSerializer.toJson(responseObject);

}
