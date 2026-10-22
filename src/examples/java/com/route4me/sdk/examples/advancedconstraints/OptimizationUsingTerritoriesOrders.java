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
public class OptimizationUsingTerritoriesOrders {

 

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
            parameters.setRouteName("Single Depot, Multiple Driver - 3 Territories Order IDs");
            parameters.setDeviceType(DeviceType.WEB.toString());
            parameters.setTravelMode(TravelMode.DRIVING.toString());
            optParameters.setParameters(parameters);

            // Depot
            Address depot = new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", 38.141598, -85.793846);
            List<Object> depots = Arrays.asList(depot);
            parameters.setDepots(depots);
        
        
            // Territories
            // **********************
            List<String> zone1 = Arrays.asList("584BE87F75599DD8B9F038D81A33F5A8");
            List<String> zone2 = Arrays.asList("A5C1B85BCC069FF6CBDA18A65DC95031");
            List<String> zone3 = Arrays.asList("007AE22B494A149B52A45E3C6D3EC0CA");

            
            
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
            schedule1.setTags(zone1);
            schedule1.setMembersCount(3);
            List<List<Integer>> timeWindowsSchedule1 = new ArrayList<>();
            List<Integer> timeWindowSchedule1 = Arrays.asList((8 + 5) * 3600 , (11 + 5) * 3600);
            timeWindowsSchedule1.add(timeWindowSchedule1);
            schedule1.setAvailableTimeWindows(timeWindowsSchedule1);


            // Schedule 2
            // Time Window Start:  8:00 am EST
            // Time Window End:   12:00 pm EST
            AdvancedConstraints schedule2 = new AdvancedConstraints();
            schedule2.setTags(zone2);
            schedule2.setMembersCount(4);
            List<List<Integer>> timeWindowsSchedule2 = new ArrayList<>();
            List<Integer> timeWindowSchedule2 = Arrays.asList((8 + 5) * 3600 , (12 + 5) * 3600);
            timeWindowsSchedule2.add(timeWindowSchedule2);
            schedule2.setAvailableTimeWindows(timeWindowsSchedule2);


            // Schedule 3
            // Time Window Start:  8:00 am EST
            // Time Window End:    01:00 pm EST
            AdvancedConstraints schedule3 = new AdvancedConstraints();
            schedule3.setTags(zone3);
            schedule3.setMembersCount(3);
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
            Logger.getLogger(OptimizationUsingTerritoriesOrders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}