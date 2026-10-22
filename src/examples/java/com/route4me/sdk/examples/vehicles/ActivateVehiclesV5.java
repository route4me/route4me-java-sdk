package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleJobResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Activates vehicles in one asynchronous job with {@link VehiclesManagerV5#activateVehicles}, then reads the job
 * status with {@link VehiclesManagerV5#getVehicleJobStatus} (and, once the job is over,
 * {@link VehiclesManagerV5#getVehicleJobResult}). The same pattern applies to
 * {@link VehiclesManagerV5#deactivateVehicles}, {@link VehiclesManagerV5#deleteVehicles} and
 * {@link VehiclesManagerV5#restoreVehicles}. Arguments: the vehicle IDs.
 */
public class ActivateVehiclesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            String[] vehicleIds = args.length > 0 ? args : new String[] {"VEHICLE_ID_HERE"};
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
