package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.Vehicles;
import java.util.List;

/**
 * Lists the vehicles with the deprecated {@link com.route4me.sdk.services.vehicles.VehiclesManager#getVehicles} of the
 * older vehicle API. New code uses {@code VehiclesManagerV5.getVehicles}; see {@link GetVehiclesV5}.
 */
public class GetVehicles {
    // VehiclesManager is deprecated (obsolete in the C# SDK); this example shows it until it is removed. It is named in
    // full because Java 8 warns on an import of a deprecated class.
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        com.route4me.sdk.services.vehicles.VehiclesManager manager =
                new com.route4me.sdk.services.vehicles.VehiclesManager(apiKey);
        try {
            List<Vehicles> vehicles = manager.getVehicles();
            if (vehicles != null) {
                for (Vehicles vehicle : vehicles) {
                    System.out.println(vehicle);
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
