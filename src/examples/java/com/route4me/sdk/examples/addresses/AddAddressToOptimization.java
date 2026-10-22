package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddAddressToOptimization {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
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
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300));
        addresses.add(new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300));
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
        
        try {
            DataObject optimizationUpdated = manager.reOptimization(optParameters, optimizationResponse);
            System.out.println("Optimization Problem ID:" + optimizationUpdated.getOptimizationProblemId());
            System.out.println("State:" + Constants.OptimizationState.get(optimizationUpdated.getState().intValue()));
            if (optimizationResponse.getAddresses() != null) {
                for (Address address : optimizationUpdated.getAddresses()) {
                    System.out.println(address.getAddress());
                }
            }
        } catch (APIException ex) {
            Logger.getLogger(AddAddressToOptimization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AddAddressToOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
