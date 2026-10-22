package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleJobResponse;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

import java.util.ArrayList;
import java.util.List;

/**
 * Renames several vehicles in one asynchronous job with {@link VehiclesManagerV5#updateVehicles}, then reads the
 * job status with {@link VehiclesManagerV5#getVehicleJobStatus}. Arguments: the vehicle IDs.
 */
public class UpdateVehiclesBulkV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            List<VehicleV5> vehicles = new ArrayList<>();
            for (String vehicleId : args.length > 0 ? args : new String[] {"VEHICLE_ID_HERE"}) {
                VehicleV5 vehicle = new VehicleV5();
                vehicle.setVehicleId(vehicleId);
                vehicle.setVehicleAlias("Renamed " + vehicleId);
                vehicles.add(vehicle);
            }
            AsyncJob job = manager.updateVehicles(vehicles);
            System.out.println("Update accepted as job " + job.getJobId());
            if (job.getJobId() != null) {
                VehicleJobResponse status = manager.getVehicleJobStatus(job.getJobId());
                System.out.println("Job status: " + status.getStatus());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
