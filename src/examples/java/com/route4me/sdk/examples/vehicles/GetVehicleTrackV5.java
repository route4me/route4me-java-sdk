package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleTrackPoint;
import com.route4me.sdk.services.vehicles.VehicleTrackResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/** Prints the track of a vehicle between two days with {@link VehiclesManagerV5#getVehicleTrack}. */
public class GetVehicleTrackV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        String vehicleId = args.length > 0 ? args[0] : "VEHICLE_ID_HERE";
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleTrackResponse track = manager.getVehicleTrack(vehicleId, "2026-10-01", "2026-10-02");
            if (track.getData() != null) {
                for (VehicleTrackPoint point : track.getData()) {
                    System.out.println(point.getTimestamp() + ": " + point.getLat() + ", " + point.getLng());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
