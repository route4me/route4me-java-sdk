/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.Vehicles;
import com.route4me.sdk.services.vehicles.VehiclesManager;

/**
 *
 * @author juan
 */
public class GetVehicle {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        VehiclesManager manager = new VehiclesManager(apiKey);
        String vehicleID = "8B3E269EE1409D469A639AC90CE0D741";
        try {
            Vehicles vehicle = manager.getVehicle(vehicleID);
            System.out.println(vehicle);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
    
}
