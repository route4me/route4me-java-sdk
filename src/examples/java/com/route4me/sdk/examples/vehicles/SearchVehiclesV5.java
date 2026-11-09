package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleSearchRequest;
import com.route4me.sdk.services.vehicles.VehicleSearchResult;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

import java.util.List;

/**
 * Finds how far some vehicles are from a location with {@link VehiclesManagerV5#searchVehicles}. Arguments: the
 * vehicle IDs; without them, the example reads the comma-separated IDs in {@code VEHICLE_IDS}.
 */
public class SearchVehiclesV5 {
    public static void main(String[] args) {
        String text = args.length > 0 ? String.join(",", args) : System.getenv("VEHICLE_IDS");
        List<String> ids = ExampleInput.toList(text);
        if (ids.isEmpty()) {
            System.err.println("Pass vehicle IDs as arguments or set VEHICLE_IDS to the comma-separated IDs of some"
                    + " of your vehicles; this example finds how far they are from a location.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleSearchRequest request = new VehicleSearchRequest()
                    .setVehicleIds(ids)
                    .setLat(40.7128).setLng(-74.0060);
            VehicleSearchResult[] found = manager.searchVehicles(request);
            for (VehicleSearchResult result : found) {
                System.out.println(result.getVehicle().getVehicleAlias() + ": " + result.getDistance());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
