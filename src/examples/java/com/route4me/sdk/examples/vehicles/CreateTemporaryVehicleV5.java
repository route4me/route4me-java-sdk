package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.AssignedVehicle;
import com.route4me.sdk.services.vehicles.AssignedVehiclesResponse;
import com.route4me.sdk.services.vehicles.VehicleTemporaryRequest;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Assigns a vehicle to a member for the next 24 hours with {@link VehiclesManagerV5#createTemporaryVehicle}.
 * Arguments: the vehicle ID and the member ID.
 */
public class CreateTemporaryVehicleV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        String vehicleId = args.length > 0 ? args[0] : "VEHICLE_ID_HERE";
        long memberId = args.length > 1 ? Long.parseLong(args[1]) : 1L;
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            long tomorrow = System.currentTimeMillis() / 1000L + 24L * 60L * 60L;
            VehicleTemporaryRequest request = new VehicleTemporaryRequest().setVehicleId(vehicleId)
                    .setAssignedMemberId(memberId).setExpiresAt(tomorrow);
            AssignedVehiclesResponse assigned = manager.createTemporaryVehicle(request);
            if (assigned.getData() != null) {
                for (AssignedVehicle a : assigned.getData()) {
                    System.out.println(a.getVehicleId() + " assigned to " + a.getAssignedMemberId()
                            + " until " + a.getExpiresAt());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
