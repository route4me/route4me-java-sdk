/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.turns;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Constants;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juan
 */
public class SingleDepotMultipleDriverLeftTurn {

 

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey, true);
        OptimizationParameters optParameters = new OptimizationParameters();

 

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.CVRP_TW_SD.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime(0);
        parameters.setParts(20);
        parameters.setRouteMaxDuration(86400 * 5);//5 Days
        parameters.setVehicleCapacity("100");
        parameters.setVehicleMaxDistanceMi("10000");
        parameters.setRouteName("Single Depot, Multiple Driver - Left Turn - Forbid");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());
        parameters.setLeftTurn(Constants.LeftTurn.LEFTTURN_FORBID.getValue());
        optParameters.setParameters(parameters);

 

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 300));
        addresses.add(new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300));
        addresses.add(new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864, 300));
        addresses.add(new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300));
        addresses.add(new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300));
        addresses.add(new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300));
        addresses.add(new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300));
        addresses.add(new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300));
        addresses.add(new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214", 38.162472, -85.792854, 300));

 
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
    }
}