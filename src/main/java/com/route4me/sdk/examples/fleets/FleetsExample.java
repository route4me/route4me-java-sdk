/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.fleets;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
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
import com.route4me.sdk.services.routing.fleets.Fleet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class FleetsExample {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.FLEETS_CVRP_TW.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime(0);
        parameters.setParts(20);
        parameters.setRouteMaxDuration(86400);
        parameters.setVehicleCapacity("100");
        parameters.setVehicleMaxDistanceMi("10000");
        parameters.setRouteName("Fleet Example - Single Depot, Multiple Driver");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());

        //Tags
        List<String> tags1 = Arrays.asList("TAG001", "TAG002");
        List<String> tags2 = Arrays.asList("TAG003");

        // Feets
        // Fleet 1
        Fleet fleet1 = new Fleet();
        fleet1.setMaxCapacity(200);
        fleet1.setMembersCount(10);
        fleet1.setTags(tags1);
        List<List<Integer>> timeWindowsFleet1 = new ArrayList<>();
        List<Integer> timeWindowFleet1 = Arrays.asList(25200, 75000);
        timeWindowsFleet1.add(timeWindowFleet1);
        fleet1.setAvailableTimeWindows(timeWindowsFleet1);
        // Fleet 2
        Fleet fleet2 = new Fleet();
        fleet2.setMaxCapacity(200);
        fleet2.setMembersCount(10);
        fleet2.setTags(tags2);
        List<List<Integer>> timeWindowsFleet2 = new ArrayList<>();
        List<Integer> timeWindowFleet2 = Arrays.asList(45200, 95000);
        timeWindowsFleet2.add(timeWindowFleet2);
        fleet2.setAvailableTimeWindows(timeWindowsFleet2);


        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);
        
        parameters.setFleets(fleets);
        
        optParameters.setParameters(parameters);

        List<Address> addresses = new ArrayList<>();
        // Depot
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 300));
        // Stops
        Address address = new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300);
        address.setTags(tags1);
        addresses.add(address);
        address = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864, 300);
        address.setTags(tags2);
        addresses.add(address);
        address = new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300);
        address.setTags(tags1);
        addresses.add(address);
        address = new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300);
        address.setTags(tags2);
        addresses.add(address);
        address = new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300);
        address.setTags(tags1);
        addresses.add(address);
        address = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300);
        address.setTags(tags2);
        addresses.add(address);
        address = new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300);
        address.setTags(tags1);
        addresses.add(address);
        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300);
        address.setTags(tags1);
        addresses.add(address);
        address = new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214", 38.162472, -85.792854, 300);
        address.setTags(tags2);
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
