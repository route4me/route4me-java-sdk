package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleJobResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;
import java.util.List;

/**
 * Activates vehicles in one asynchronous job with {@link VehiclesManagerV5#activateVehicles}, then reads the job
 * status with {@link VehiclesManagerV5#getVehicleJobStatus} (and, once the job is over,
 * {@link VehiclesManagerV5#getVehicleJobResult}). The same pattern applies to
 * {@link VehiclesManagerV5#deactivateVehicles}, {@link VehiclesManagerV5#deleteVehicles} and
 * {@link VehiclesManagerV5#restoreVehicles}. Arguments: the vehicle IDs; without them, the example reads the
 * comma-separated IDs in {@code VEHICLE_IDS}. Use vehicles of a test account: the example activates them.
 */
public class ActivateVehiclesV5 {
    public static void main(String[] args) {
        String text = args.length > 0 ? String.join(",", args) : System.getenv("VEHICLE_IDS");
        List<String> ids = ExampleInput.toList(args.length > 0 ? "The argument list" : "VEHICLE_IDS", text);
        if (ids == null) {
            return;
        }
        if (ids.isEmpty()) {
            System.err.println("Pass vehicle IDs as arguments or set VEHICLE_IDS to the comma-separated IDs of"
                    + " vehicles of a test account; this example activates them.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            String[] vehicleIds = ids.toArray(new String[0]);
            AsyncJob job = manager.activateVehicles(vehicleIds);
            System.out.println("Activation accepted as job " + job.getJobId());
            if (job.getJobId() != null) {
                VehicleJobResponse status = manager.getVehicleJobStatus(job.getJobId());
                System.out.println("Job status: " + status.getStatus());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
