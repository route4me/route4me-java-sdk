package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.Optimization;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddRemoveAddressReOtimizeOptimization {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(Constants.AlgorithmType.TSP.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteName("Single Depot, Single Driver - Adding Addresses Post Optimization");
        parameters.setOptimize(Constants.Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(Constants.DistanceUnit.MI.toString());
        parameters.setDeviceType(Constants.DeviceType.WEB.toString());
        parameters.setTravelMode(Constants.TravelMode.DRIVING.toString());
        optParameters.setParameters(parameters);

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 300));
        addresses.add(new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300));
        addresses.add(new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864, 300));
        addresses.add(new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300));
        addresses.add(new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300));
        optParameters.setAddresses(addresses);

        // Creating Optimization
        
        DataObject optimizationResponse = new DataObject();
        
        
        try {
            optimizationResponse = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID:" + optimizationResponse.getOptimizationProblemId());
            System.out.println("State:" + Constants.OptimizationState.get(optimizationResponse.getState().intValue()));
            if (optimizationResponse.getAddresses() != null) {
                for (Address address : optimizationResponse.getAddresses()) {
                    System.out.println(address.getAddress());
                }
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
        
        
        // Adding More Addresses to Optimization
        System.out.println("\nAdding More Stops and Reoptimizing");
        
        optimizationResponse.getAddresses().add(new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300));
        optimizationResponse.getAddresses().add(new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300));
        optimizationResponse.getAddresses().add(new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214", 38.162472, -85.792854, 300));

        optParameters = new OptimizationParameters();
        optParameters.setProblemId(optimizationResponse.getOptimizationProblemId());
        optParameters.setReoptimize(true);
        
        Optimization optimizationUpdated = new Optimization();
        
        try {
            optimizationUpdated = manager.reOptimization(optParameters, optimizationResponse);
            System.out.println("Optimization Problem ID:" + optimizationUpdated.getOptimizationProblemId());
            System.out.println("State:" + Constants.OptimizationState.get(optimizationUpdated.getState().intValue()));
            if (optimizationResponse.getAddresses() != null) {
                for (Address address : optimizationUpdated.getAddresses()) {
                    System.out.println(address.getAddress());
                }
            }
        } catch (APIException ex) {
            Logger.getLogger(AddRemoveAddressReOtimizeOptimization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AddRemoveAddressReOtimizeOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        // Removing Stops from Optimization
        System.out.println("\nRemoving an Address from Optimization");

        
        Address addressToBeRemoved = optimizationUpdated.getRoutes().get(0).getAddresses().get(5);
        
        System.out.println("Removing Address: " + addressToBeRemoved.getAddress() + " "  + addressToBeRemoved.getRouteDestinationId());

        try {
            manager.deleteAddressFromOptimization(optimizationUpdated.getOptimizationProblemId(), addressToBeRemoved.getRouteDestinationId());
        } catch (APIException ex) {
            Logger.getLogger(AddRemoveAddressReOtimizeOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }

        optParameters = new OptimizationParameters();
        optParameters.setProblemId(optimizationResponse.getOptimizationProblemId());
        optParameters.setReoptimize(true);
        
        try {
            optimizationUpdated = manager.reOptimizationByOptimizationID(optParameters);
            System.out.println("Optimization Problem ID:" + optimizationUpdated.getOptimizationProblemId());
            System.out.println("State:" + Constants.OptimizationState.get(optimizationUpdated.getState().intValue()));
            if (optimizationResponse.getAddresses() != null) {
                for (Address address : optimizationUpdated.getAddresses()) {
                    System.out.println(address.getAddress());
                }
            }
        } catch (APIException ex) {
            Logger.getLogger(AddRemoveAddressReOtimizeOptimization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AddRemoveAddressReOtimizeOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       // Getting Updated Optimization
       System.out.println("\nGetting Updated Optimization");

       optParameters = new OptimizationParameters();
       optParameters.setProblemId(optimizationResponse.getOptimizationProblemId());
        try {
            optimizationResponse = manager.getOptimization(optParameters);
        } catch (APIException ex) {
            Logger.getLogger(AddRemoveAddressReOtimizeOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        // Adding Again more Stops 
        
        System.out.println("\nAdding Even More Stops and Reoptimizing");

        
        optimizationResponse.getAddresses().add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300));
        optimizationResponse.getAddresses().add(new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300));

        optParameters = new OptimizationParameters();
        optParameters.setProblemId(optimizationUpdated.getOptimizationProblemId());
        optParameters.setReoptimize(true);
        
        try {
            optimizationUpdated = manager.reOptimization(optParameters, optimizationResponse);
            System.out.println("Optimization Problem ID:" + optimizationUpdated.getOptimizationProblemId());
            System.out.println("State:" + Constants.OptimizationState.get(optimizationUpdated.getState().intValue()));
            if (optimizationResponse.getAddresses() != null) {
                for (Address address : optimizationUpdated.getAddresses()) {
                    System.out.println(address.getAddress());
                }
            }
        } catch (APIException ex) {
            Logger.getLogger(AddRemoveAddressReOtimizeOptimization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AddRemoveAddressReOtimizeOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

}
