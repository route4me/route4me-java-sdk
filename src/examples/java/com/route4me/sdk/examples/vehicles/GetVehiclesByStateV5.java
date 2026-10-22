package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleListRequest;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Lists the vehicles in one state ({@code active}, {@code pending}, {@code deleted} or {@code all}) with
 * {@link VehiclesManagerV5#getVehicles}, through the {@code show} filter of {@link VehicleListRequest}.
 */
public class GetVehiclesByStateV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        String state = args.length > 0 ? args[0] : "deleted";
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleV5[] vehicles = manager.getVehicles(new VehicleListRequest().setShow(state));
            System.out.println(vehicles.length + " " + state + " vehicles");
            for (VehicleV5 v : vehicles) {
                System.out.println("Vehicle: " + v.getVehicleAlias() + " (ID: " + v.getVehicleId() + ")");
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
