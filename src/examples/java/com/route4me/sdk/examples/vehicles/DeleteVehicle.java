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
public class DeleteVehicle {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManager manager = new VehiclesManager(apiKey);
        String vehicleID = "00F86E30FEC8DAF61A674760E926086F";
        try {
            Vehicles vehicle = manager.deleteVehicle(vehicleID);
            System.out.println(vehicle);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }


    
}
