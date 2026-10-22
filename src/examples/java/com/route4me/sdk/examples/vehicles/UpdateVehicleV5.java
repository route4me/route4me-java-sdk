package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/** Changes the alias of a vehicle through the V5 API with {@link VehiclesManagerV5#updateVehicle}. */
public class UpdateVehicleV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        String vehicleId = args.length > 0 ? args[0] : "VEHICLE_ID_HERE";
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
