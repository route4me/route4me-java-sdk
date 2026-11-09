package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Gets one vehicle with {@link VehiclesManagerV5#getVehicleById}, which replaces the deprecated
 * {@code VehiclesManager.getVehicle}. Set {@code VEHICLE_ID} to a vehicle of the account.
 */
public class GetVehicle {
    public static void main(String[] args) {
        String vehicleID = System.getenv("VEHICLE_ID");
        if (vehicleID == null || vehicleID.isEmpty()) {
            System.err.println("Set VEHICLE_ID to the ID of a vehicle of the account.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleV5 vehicle = manager.getVehicleById(vehicleID);
            System.out.println(vehicle);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
