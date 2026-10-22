package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.Vehicles;

/**
 * Reads a vehicle with the deprecated {@link com.route4me.sdk.services.vehicles.VehiclesManager#getVehicle} and
 * changes it with {@link com.route4me.sdk.services.vehicles.VehiclesManager#updateVehicle}, of the older vehicle API.
 * New code uses {@code VehiclesManagerV5.updateVehicle}; see {@link UpdateVehicleV5}. Set {@code VEHICLE_ID} to a
 * vehicle of a test account: the example changes its alias.
 */
public class UpdateVehicle {
    // VehiclesManager is deprecated (obsolete in the C# SDK); this example shows it until it is removed. It is named in
    // full because Java 8 warns on an import of a deprecated class.
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        String vehicleID = System.getenv("VEHICLE_ID");
        if (vehicleID == null || vehicleID.isEmpty()) {
            System.err.println("Set VEHICLE_ID to a vehicle of a test account; this example changes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        com.route4me.sdk.services.vehicles.VehiclesManager manager =
                new com.route4me.sdk.services.vehicles.VehiclesManager(apiKey);
        try {
            Vehicles vehicle = manager.getVehicle(vehicleID);
            if (vehicle == null) {
                System.err.println("No vehicle has the ID " + vehicleID);
                return;
            }
            vehicle.setVehicleAlias("Example vehicle, renamed");
            vehicle = manager.updateVehicle(vehicle);
            System.out.println(vehicle);

        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
