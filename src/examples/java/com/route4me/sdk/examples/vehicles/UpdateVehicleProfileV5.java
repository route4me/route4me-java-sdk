package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleProfile;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Renames a vehicle profile with {@link VehiclesManagerV5#updateVehicleProfile}. Argument: the profile ID; without
 * it, the example reads {@code VEHICLE_PROFILE_ID}. Use a vehicle profile of a test account: the example renames it.
 */
public class UpdateVehicleProfileV5 {
    public static void main(String[] args) {
        String profileId = args.length > 0 ? args[0] : System.getenv("VEHICLE_PROFILE_ID");
        if (profileId == null || profileId.isEmpty()) {
            System.err.println("Pass the profile ID as an argument or set VEHICLE_PROFILE_ID to a vehicle profile of a"
                    + " test account; this example renames it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
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
