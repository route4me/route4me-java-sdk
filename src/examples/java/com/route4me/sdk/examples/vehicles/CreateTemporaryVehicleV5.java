package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.AssignedVehicle;
import com.route4me.sdk.services.vehicles.AssignedVehiclesResponse;
import com.route4me.sdk.services.vehicles.VehicleTemporaryRequest;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Assigns a vehicle to a member for the next 24 hours with {@link VehiclesManagerV5#createTemporaryVehicle}.
 * Arguments: the vehicle ID and the member ID; without them, the example reads {@code VEHICLE_ID} and
 * {@code MEMBER_ID}. Use a vehicle and a member of a test account.
 */
public class CreateTemporaryVehicleV5 {
    public static void main(String[] args) {
        String vehicleId = args.length > 0 ? args[0] : System.getenv("VEHICLE_ID");
        String memberId = args.length > 1 ? args[1] : System.getenv("MEMBER_ID");
        if (vehicleId == null || vehicleId.isEmpty() || memberId == null || memberId.isEmpty()) {
            System.err.println("Pass the vehicle ID and the member ID as arguments, or set VEHICLE_ID and MEMBER_ID"
                    + " to a vehicle and a member of a test account; this example assigns the vehicle to the member"
                    + " for 24 hours.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            long tomorrow = System.currentTimeMillis() / 1000L + 24L * 60L * 60L;
            VehicleTemporaryRequest request = new VehicleTemporaryRequest().setVehicleId(vehicleId)
                    .setAssignedMemberId(Long.parseLong(memberId)).setExpiresAt(tomorrow);
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
