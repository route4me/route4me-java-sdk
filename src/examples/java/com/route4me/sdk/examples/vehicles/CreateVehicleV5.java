package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/** Creates a vehicle through the V5 API with {@link VehiclesManagerV5#createVehicle}. */
public class CreateVehicleV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleV5 vehicle = new VehicleV5();
            vehicle.setVehicleAlias("Test Vehicle");
            vehicle.setVehicleMake("Ford");
            vehicle.setVehicleModel("Transit");
            vehicle.setVehicleModelYear(2024);
            vehicle.setVehicleTypeId("van");
            vehicle.setFuelType("diesel");
            VehicleV5 result = manager.createVehicle(vehicle);
            System.out.println("Created vehicle: " + result.getVehicleId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
