package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleCapacityProfile;
import com.route4me.sdk.services.vehicles.VehicleCapacityProfileResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Changes the maximum weight of a vehicle capacity profile with
 * {@link VehiclesManagerV5#updateVehicleCapacityProfile}. Argument: the capacity profile ID.
 */
public class UpdateVehicleCapacityProfileV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        String profileId = args.length > 0 ? args[0] : "CAPACITY_PROFILE_ID_HERE";
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
