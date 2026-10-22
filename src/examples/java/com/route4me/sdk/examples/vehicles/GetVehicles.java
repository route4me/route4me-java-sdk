/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehiclesManager;
import com.route4me.sdk.services.vehicles.Vehicles;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetVehicles {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManager manager = new VehiclesManager(apiKey);
        try {
            List<Vehicles> vehicles = manager.getVehicles();
            for (Vehicles vehicle : vehicles) {
                System.out.println(vehicle);
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
    
}
