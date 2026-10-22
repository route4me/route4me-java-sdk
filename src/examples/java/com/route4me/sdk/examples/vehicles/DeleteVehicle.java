package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Deletes a vehicle with {@link VehiclesManagerV5#deleteVehicle}, which replaces the deprecated
 * {@code VehiclesManager.deleteVehicle}. Set {@code VEHICLE_ID} to a vehicle of a test account: the example deletes it.
 */
public class DeleteVehicle {

    public static void main(String[] args) {
        String vehicleID = System.getenv("VEHICLE_ID");
        if (vehicleID == null || vehicleID.isEmpty()) {
            System.err.println("Set VEHICLE_ID to a vehicle of a test account; this example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleV5 vehicle = manager.deleteVehicle(vehicleID);
            System.out.println(vehicle);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
