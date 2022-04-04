/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Constants.AlgorithmType;
import com.route4me.sdk.services.routing.Constants.DeviceType;
import com.route4me.sdk.services.routing.Constants.OptimizationState;
import com.route4me.sdk.services.routing.Constants.TravelMode;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class SingleDepotMultipleDriverByTerritoryOrderIDs {

 

    public static void main(String[] args) {
        try {
            String apiKey = System.getenv("R4M_API_KEY");
            RoutingManager manager = new RoutingManager(apiKey, true);
            OptimizationParameters optParameters = new OptimizationParameters();
            
            TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);
            
            
            Parameters parameters = new Parameters();
            parameters.setAlgorithmType(AlgorithmType.CVRP_TW_SD.getValue());
            parameters.setStoreRoute(Boolean.FALSE);
            parameters.setShareRoute(Boolean.FALSE);
            parameters.setRouteTime(0);
            parameters.setRouteName("Single Depot, Multiple Driver - By Order IDs");
            parameters.setDeviceType(DeviceType.WEB.toString());
            parameters.setTravelMode(TravelMode.DRIVING.toString());
            optParameters.setParameters(parameters);


            List<Address> addresses = new ArrayList<>();

            
            Territory territory = territoriesManager.getOrdersInTerritory("04F6ADD89DB67371E9008A6688B750C0");
            for (Integer setOrderId:  territory.getOrders()){
                Address address = new Address();
                address.setOrderId(setOrderId);
                addresses.add(address);
            }
            
            optParameters.setAddresses(addresses);
            
            try {
                DataObject responseObject = manager.runOptimization(optParameters);
                System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
                System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
                if (responseObject.getAddresses() != null) {
                    for (Address address : responseObject.getAddresses()) {
                        System.out.println(address);
                    }
                }
            } catch (APIException e) {
                //handle exception
                e.printStackTrace();
            }
        } catch (APIException ex) {
            Logger.getLogger(SingleDepotMultipleDriverByTerritoryOrderIDs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}