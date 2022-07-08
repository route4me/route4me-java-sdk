/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.advancedconstraints;

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
import com.route4me.sdk.services.routing.advancedconstraints.AdvancedConstraints;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class OptimizationUsingTerritoriesOrdersDifferentDepot {

 

    public static void main(String[] args) {
        try {
            String apiKey = System.getenv("R4M_API_KEY");
            RoutingManager manager = new RoutingManager(apiKey, true);
            OptimizationParameters optParameters = new OptimizationParameters();

            //**********************************************************************
            // TEST CASE: Optimization using Territories Orders
            // 3 Territories
            //**********************************************************************
            
            TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);
            
            
            Parameters parameters = new Parameters();
            parameters.setAlgorithmType(AlgorithmType.ADVANCED_CVRP_TW.getValue());
            parameters.setStoreRoute(Boolean.FALSE);
            parameters.setShareRoute(Boolean.FALSE);
            parameters.setRouteTime((8 + 5) * 3600);
            parameters.setRouteName("Multiple Depot, Multiple Driver - 3 Territories Order IDs");
            parameters.setDeviceType(DeviceType.WEB.toString());
            parameters.setTravelMode(TravelMode.DRIVING.toString());
            optParameters.setParameters(parameters);

        
            // Territories
            // **********************
            List<String> zone1 = Arrays.asList("477FF1EB618FDE5C1CA85292B2A6DE92");
            List<String> zone2 = Arrays.asList("2A6E027E74A3623762BEA3D6E94B643E");
            List<String> zone3 = Arrays.asList("C8B7DEFFC4C42AB7674EA27CBC607B3F");

            
            
            List<Address> addresses = new ArrayList<>();

            Address address;
            
            Territory territory = territoriesManager.getOrdersInTerritory(zone1.get(0));
            for (Integer orderId:  territory.getOrders()){
                address = new Address();
                address.setOrderId(orderId);
                address.setTags(zone1);
                addresses.add(address);
            }

            territory = territoriesManager.getOrdersInTerritory(zone2.get(0));
            for (Integer orderId:  territory.getOrders()){
                address = new Address();
                address.setOrderId(orderId);
                address.setTags(zone2);
                addresses.add(address);
            }

            territory = territoriesManager.getOrdersInTerritory(zone3.get(0));
            for (Integer orderId:  territory.getOrders()){
                address = new Address();
                address.setTags(zone3);
                address.setOrderId(orderId);
                addresses.add(address);
            }

            AdvancedConstraints schedule1 = new AdvancedConstraints();
            schedule1.setDepotAddress(new Address("15110 Oakmont St, Overland Park, KS 66221, USA", "DEPOT KS", 38.854268, -94.7333895, 0));
            schedule1.setTags(zone1);
            schedule1.setMembersCount(1);
            List<List<Integer>> timeWindowsSchedule1 = new ArrayList<>();
            List<Integer> timeWindowSchedule1 = Arrays.asList((8 + 5) * 3600 , (11 + 5) * 3600);
            timeWindowsSchedule1.add(timeWindowSchedule1);
            schedule1.setAvailableTimeWindows(timeWindowsSchedule1);


            // Schedule 2
            // Time Window Start:  8:00 am EST
            // Time Window End:   12:00 pm EST
            AdvancedConstraints schedule2 = new AdvancedConstraints();
            schedule2.setDepotAddress(new Address("754 5th Ave, New York, NY 10019, USA", "DEPOT NY", 40.7636197, -73.9744388, 0));
            schedule2.setTags(zone2);
            schedule2.setMembersCount(1);
            List<List<Integer>> timeWindowsSchedule2 = new ArrayList<>();
            List<Integer> timeWindowSchedule2 = Arrays.asList((8 + 5) * 3600 , (12 + 5) * 3600);
            timeWindowsSchedule2.add(timeWindowSchedule2);
            schedule2.setAvailableTimeWindows(timeWindowsSchedule2);


            // Schedule 3
            // Time Window Start:  8:00 am EST
            // Time Window End:    01:00 pm EST
            AdvancedConstraints schedule3 = new AdvancedConstraints();
            schedule3.setDepotAddress(new Address("106 W Columbus Dr, Tampa, FL 33602, USA", "DEPOT FL", 27.9664433, -82.4564934, 0));

            schedule3.setTags(zone3);
            schedule3.setMembersCount(1);
            List<List<Integer>> timeWindowsSchedule3 = new ArrayList<>();
            List<Integer> timeWindowSchedule3 = Arrays.asList((8 + 5) * 3600 , (13 + 5) * 3600);
            timeWindowsSchedule3.add(timeWindowSchedule3);
            schedule3.setAvailableTimeWindows(timeWindowsSchedule3);


            // Schedules registration
            List<AdvancedConstraints> advancedConstraints = Arrays.asList(schedule1, schedule2, schedule3);       
            parameters.setAdvancedConstraints(advancedConstraints);
            
            optParameters.setAddresses(addresses);
            
            try {
                DataObject responseObject = manager.runOptimization(optParameters);
                System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
                System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
                if (responseObject.getAddresses() != null) {
                    for (Address addressResponse : responseObject.getAddresses()) {
                        System.out.println(addressResponse);
                    }
                }
            } catch (APIException e) {
                //handle exception
                e.printStackTrace();
            }
        } catch (APIException ex) {
            Logger.getLogger(OptimizationUsingTerritoriesOrdersDifferentDepot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}