package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Changes the alias of a vehicle through the V5 API with {@link VehiclesManagerV5#updateVehicle}. Argument: the
 * vehicle ID; without it, the example reads {@code VEHICLE_ID}. Use a vehicle of a test account: the example
 * changes it.
 */
public class UpdateVehicleV5 {
    public static void main(String[] args) {
        String vehicleId = args.length > 0 ? args[0] : System.getenv("VEHICLE_ID");
        if (vehicleId == null || vehicleId.isEmpty()) {
            System.err.println("Pass the vehicle ID as an argument or set VEHICLE_ID to a vehicle of a test account;"
                    + " this example changes its alias.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleV5 change = new VehicleV5();
            change.setVehicleAlias("Renamed vehicle");
            VehicleV5 updated = manager.updateVehicle(vehicleId, change);
            System.out.println("Updated vehicle: " + updated.getVehicleAlias());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
