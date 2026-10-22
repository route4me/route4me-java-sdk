package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.Vehicles;

/**
 * Creates a vehicle with the deprecated {@link com.route4me.sdk.services.vehicles.VehiclesManager#newVehicle} of the
 * older vehicle API. New code uses {@code VehiclesManagerV5.createVehicle}; see {@link CreateVehicleV5}.
 */
public class NewVehicle {
    // VehiclesManager is deprecated (obsolete in the C# SDK); this example shows it until it is removed. It is named in
    // full because Java 8 warns on an import of a deprecated class.
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        com.route4me.sdk.services.vehicles.VehiclesManager manager =
                new com.route4me.sdk.services.vehicles.VehiclesManager(apiKey);
        try {
            Vehicles vehicle = new Vehicles();
            vehicle.setVehicleAlias("Camry");
            vehicle.setVehicleVin("JS3TD62V1Y4444444");
            vehicle.setVehicleModelYear(1995);
            vehicle.setVehicleMake(Vehicles.VehicleMake.TOYOTA.getValue());
            vehicle.setFuelType("unleaded 93");
            vehicle = manager.newVehicle(vehicle);
            System.out.println(vehicle);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
