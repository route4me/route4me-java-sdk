package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleOrderRequest;
import com.route4me.sdk.services.vehicles.VehicleOrderResponse;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Executes the order of a vehicle at a location with {@link VehiclesManagerV5#executeVehicleOrder}. Argument: the
 * vehicle ID; without it, the example reads {@code VEHICLE_ID}. Use a vehicle of a test account.
 */
public class ExecuteVehicleOrderV5 {
    public static void main(String[] args) {
        String vehicleId = args.length > 0 ? args[0] : System.getenv("VEHICLE_ID");
        if (vehicleId == null || vehicleId.isEmpty()) {
            System.err.println("Pass the vehicle ID as an argument or set VEHICLE_ID to a vehicle of a test account;"
                    + " this example executes an order for it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleOrderResponse order = manager.executeVehicleOrder(
                    new VehicleOrderRequest().setVehicleId(vehicleId).setLat(40.7128).setLng(-74.0060));
            System.out.println("Vehicle " + order.getVehicleId() + ", order " + order.getOrderId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
