package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/** Finds a vehicle by its license plate with {@link VehiclesManagerV5#getVehicleByLicensePlate}. */
public class GetVehicleByLicensePlateV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        String licensePlate = args.length > 0 ? args[0] : "EX123";
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleResponse found = manager.getVehicleByLicensePlate(licensePlate);
            if (found.getData() != null && found.getData().getVehicle() != null) {
                System.out.println("Vehicle: " + found.getData().getVehicle().getVehicleId());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
