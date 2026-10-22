package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleJobResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Activates vehicles in one asynchronous job with {@link VehiclesManagerV5#activateVehicles}, then reads the job
 * result with {@link VehiclesManagerV5#getVehicleJobResult} when the API returns a job ID. The same pattern applies
 * to {@link VehiclesManagerV5#deactivateVehicles}, {@link VehiclesManagerV5#deleteVehicles} and
 * {@link VehiclesManagerV5#restoreVehicles}. Arguments: the vehicle IDs.
 */
public class ActivateVehiclesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            String[] vehicleIds = args.length > 0 ? args : new String[] {"VEHICLE_ID_HERE"};
            VehicleJobResponse job = manager.activateVehicles(vehicleIds);
            if (job != null && job.getJobId() != null) {
                System.out.println("Job result: " + manager.getVehicleJobResult(job.getJobId()).getResult());
            } else {
                System.out.println("Activation accepted");
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
