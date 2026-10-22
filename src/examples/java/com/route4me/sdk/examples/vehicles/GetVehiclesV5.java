package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/** Lists the vehicles through the V5 API with {@link VehiclesManagerV5#getVehicles}. */
public class GetVehiclesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleV5[] vehicles = manager.getVehicles(1, 10);
            for (VehicleV5 v : vehicles) {
                System.out.println("Vehicle: " + v.getVehicleAlias() + " (ID: " + v.getVehicleId() + ")");
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
