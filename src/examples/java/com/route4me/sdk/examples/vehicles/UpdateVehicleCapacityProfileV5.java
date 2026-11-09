package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleCapacityProfile;
import com.route4me.sdk.services.vehicles.VehicleCapacityProfileResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Changes the maximum weight of a vehicle capacity profile with
 * {@link VehiclesManagerV5#updateVehicleCapacityProfile}. Argument: the capacity profile ID; without it, the example
 * reads {@code VEHICLE_CAPACITY_PROFILE_ID}. Use a capacity profile of a test account: the example changes it.
 */
public class UpdateVehicleCapacityProfileV5 {
    public static void main(String[] args) {
        String profileId = args.length > 0 ? args[0] : System.getenv("VEHICLE_CAPACITY_PROFILE_ID");
        if (profileId == null || profileId.isEmpty()) {
            System.err.println("Pass the capacity profile ID as an argument or set VEHICLE_CAPACITY_PROFILE_ID to a"
                    + " vehicle capacity profile of a test account; this example changes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleCapacityProfile change = new VehicleCapacityProfile();
            change.setMaxWeight(1500.0);
            VehicleCapacityProfileResponse updated = manager.updateVehicleCapacityProfile(profileId, change);
            if (updated.getData() != null) {
                System.out.println("Max weight: " + updated.getData().getMaxWeight());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
