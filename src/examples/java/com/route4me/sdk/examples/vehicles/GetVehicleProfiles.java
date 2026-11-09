package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleProfile;
import com.route4me.sdk.services.vehicles.VehicleProfilesResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/** Lists the vehicle profiles through the V5 API with {@link VehiclesManagerV5#getVehicleProfiles}. */
public class GetVehicleProfiles {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleProfilesResponse profiles = manager.getVehicleProfiles(1, 10);
            if (profiles.getData() != null) {
                for (VehicleProfile p : profiles.getData()) {
                    System.out.println("Profile: " + p.getName() + " (ID: " + p.getVehicleProfileId() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
