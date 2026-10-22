package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleProfile;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/** Renames a vehicle profile with {@link VehiclesManagerV5#updateVehicleProfile}. Argument: the profile ID. */
public class UpdateVehicleProfileV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        String profileId = args.length > 0 ? args[0] : "PROFILE_ID_HERE";
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleProfile change = new VehicleProfile();
            change.setName("Renamed profile");
            VehicleProfile updated = manager.updateVehicleProfile(profileId, change);
            System.out.println("Updated profile: " + updated.getName());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
