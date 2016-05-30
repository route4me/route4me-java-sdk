/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.model.Address;
import com.route4me.sdk.model.DataObject;

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
        String optimizationID = responseObject.getOptimizations().get(0).getOptimization_problem_id();
        DataObject optimization;
        optimization = optimizationManager.getOptimization(optimizationID);
        System.out.println("Optimization ID: " + optimization.getOptimization_problem_id());
        int i = 0;
        for (Address address : optimization.getAddresses()) {
            i = i + 1;
            System.out.println("Address " + i);
            System.out.println("\tAddress: " + address.getAddress());
            System.out.println("\tLatitude: " + address.getLat());
            System.out.println("\tLongitude: " + address.getLng());
            System.out.println();
        }

    }
    //String jsonResponse = Manager.GSONSerializer.toJson(responseObject);

}
