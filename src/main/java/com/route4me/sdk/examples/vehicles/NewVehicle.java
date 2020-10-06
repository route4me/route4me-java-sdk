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
public class NewVehicle {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        VehiclesManager manager = new VehiclesManager(apiKey);
        try {
            Vehicles vehicle = new Vehicles();
            vehicle.setVehicleAlias("Camry");
            vehicle.setVehicleVin("JS3TD62V1Y4444444");
            vehicle.setVehicleModelYear(1995);
            vehicle.setVehicleMake(Vehicles.VehicleMake.TOYOTA.getValue());
            vehicle.setFuelType("unleaded 93");
            vehicle = manager.newVehicle(vehicle);
            System.out.println(vehicle);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
    
}
