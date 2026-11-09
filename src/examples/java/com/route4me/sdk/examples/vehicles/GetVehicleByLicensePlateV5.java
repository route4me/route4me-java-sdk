package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Finds a vehicle by its license plate with {@link VehiclesManagerV5#getVehicleByLicensePlate}. Argument: the
 * license plate; without it, the example reads {@code LICENSE_PLATE}.
 */
public class GetVehicleByLicensePlateV5 {
    public static void main(String[] args) {
        String licensePlate = args.length > 0 ? args[0] : System.getenv("LICENSE_PLATE");
        if (licensePlate == null || licensePlate.isEmpty()) {
            System.err.println("Pass the license plate as an argument or set LICENSE_PLATE to the plate of one of your"
                    + " vehicles; this example finds the vehicle.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
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
