/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.advancedconstraints;


import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Constants.AlgorithmType;
import com.route4me.sdk.services.routing.Constants.OptimizationState;
import com.route4me.sdk.services.routing.Constants.Optimize;
import com.route4me.sdk.services.routing.Constants.TravelMode;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.advancedconstraints.AdvancedConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class DriversDifferentWorkingTime {

    //**********************************************************************
    // TEST CASE: Driver's Different Working Time
    // Drivers have 2 Working Times:
    // Full Time 8h
    // Partial Time 4h
    //**********************************************************************

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();
        
        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.ADVANCED_CVRP_TW.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime((7 + 5) * 3600); // Route Time 7am EST Time
        parameters.setParts(8);
        parameters.setRouteMaxDuration(8 * 3600);
        parameters.setRouteName("Drivers Different Working Time Example - Single Depot, Multiple Driver");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());

        // Feets
        // Full-Time 
        AdvancedConstraints advancedConstraintFullTime = new AdvancedConstraints();
        advancedConstraintFullTime.setMembersCount(4);
        List<List<Integer>> timeWindowsFullTime = new ArrayList<>();
        List<Integer> timeWindowFullTime = Arrays.asList(43200, 72000);
        timeWindowsFullTime.add(timeWindowFullTime);
        advancedConstraintFullTime.setAvailableTimeWindows(timeWindowsFullTime);
        // Part Time
        AdvancedConstraints advancedConstraintPartTime = new AdvancedConstraints();
        advancedConstraintPartTime.setMembersCount(2);
        List<List<Integer>> timeWindowsPartTime = new ArrayList<>();
        // Driver Shift
        List<Integer> timeWindowPartTime = Arrays.asList(43200, 57600);
        timeWindowsPartTime.add(timeWindowPartTime);
        advancedConstraintPartTime.setAvailableTimeWindows(timeWindowsPartTime);


        List<AdvancedConstraints> advancedConstraints = Arrays.asList(advancedConstraintFullTime, advancedConstraintPartTime);
        
        parameters.setAdvancedConstraints(advancedConstraints);
        
        optParameters.setParameters(parameters);

        List<Address> addresses = new ArrayList<>();
        // Depot
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 0));

        // Stops
        Address address = new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 3600);
        addresses.add(address);

        address = new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 3600);
        addresses.add(address);

        address = new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 3600);
        addresses.add(address);

        address = new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 3600);
        addresses.add(address);

        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 3600);
        addresses.add(address);

        address = new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214", 38.162472, -85.792854, 3600);
        addresses.add(address);

        address = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 3600);
        addresses.add(address);

        address = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864, 3600);
        addresses.add(address);

        address = new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 3600);
        addresses.add(address);
        
        
        optParameters.setAddresses(addresses);

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
