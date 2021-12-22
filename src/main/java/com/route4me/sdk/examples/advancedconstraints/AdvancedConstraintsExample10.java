/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.advancedconstraints;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.AddressSource;
import com.route4me.sdk.services.routing.Constants.AlgorithmType;
import com.route4me.sdk.services.routing.Constants.DeviceType;
import com.route4me.sdk.services.routing.Constants.DistanceUnit;
import com.route4me.sdk.services.routing.Constants.OptimizationState;
import com.route4me.sdk.services.routing.Constants.Optimize;
import com.route4me.sdk.services.routing.Constants.TravelMode;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.SourceType;
import com.route4me.sdk.services.routing.advancedconstraints.AdvancedConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class AdvancedConstraintsExample10 {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        //**********************************************************************
        // TEST CASE: Retail Location Single Depot Multiple Drivers
        //**********************************************************************

        
        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.ADVANCED_CVRP_TW.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime(0);
        parameters.setParts(20);
        parameters.setRouteMaxDuration(86400);
        parameters.setVehicleCapacity("100");
        parameters.setVehicleMaxDistanceMi("10000");
        parameters.setRouteName("Retail Location - Single Depot - Multiple Driver");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());
        
        List<Address> addresses = new ArrayList<>();
        // Depot
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 300));
        // Stops
        Address address = new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300);
        addresses.add(address);
        address = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864, 300);
        addresses.add(address);
        address = new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300);
        addresses.add(address);
        address = new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300);
        addresses.add(address);
        address = new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300);
        addresses.add(address);
        address = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300);
        addresses.add(address);
        address = new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300);
        addresses.add(address);
        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300);
        addresses.add(address);
        address = new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214", 38.162472, -85.792854, 300);
        addresses.add(address);

        optParameters.setAddresses(addresses);
        
        // ADVANCED CONSTRAINT

         //Retail Location: "4738 BELLEVUE AVE, Louisville, KY, 40215" 
        // Index 6 in the addresses list 
        // We want that our Retail location is visited at the end of the route
        // So we use the special flag "" to indicate that before visiting retail location can be any stop.
        // We can have Different patternt that allow to have more than one retail location into the routes.
        AddressSource retailLocationSource  = new AddressSource(6, SourceType.ADDRESSES);
        List <Object> locationsSequence = Arrays.asList("", retailLocationSource);

                
        // AdvancedConstraints 1
        AdvancedConstraints advancedConstraint1 = new AdvancedConstraints();
        advancedConstraint1.setMaxCapacity(200);
        advancedConstraint1.setMembersCount(10);
        List<List<Integer>> timeWindows1 = new ArrayList<>();
        List<Integer> timeWindow1 = Arrays.asList(25200, 75000);
        timeWindows1.add(timeWindow1);
        advancedConstraint1.setAvailableTimeWindows(timeWindows1);
        advancedConstraint1.setLocationSequencePattern(locationsSequence);

        // AdvancedConstraints  2
        AdvancedConstraints advancedConstraint2 = new AdvancedConstraints();
        advancedConstraint2.setMaxCapacity(200);
        advancedConstraint2.setMembersCount(10);
        List<List<Integer>> timeWindows2 = new ArrayList<>();
        List<Integer> timeWindow2 = Arrays.asList(45200, 95000);
        timeWindows2.add(timeWindow2);
        advancedConstraint2.setAvailableTimeWindows(timeWindows2);
        advancedConstraint2.setLocationSequencePattern(locationsSequence);


        List<AdvancedConstraints> advancedConstraints = Arrays.asList(advancedConstraint1, advancedConstraint2);
        
        parameters.setAdvancedConstraints(advancedConstraints);
        
        optParameters.setParameters(parameters);
       
        
        
        
        
        try {
            DataObject responseObject = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
            System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
            if (responseObject.getAddresses() != null) {
                for (Iterator<Address> it = responseObject.getAddresses().iterator(); it.hasNext();) {
                    address = it.next();
                    System.out.println(address);
                }
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }


    
}
