package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleSearchRequest;
import com.route4me.sdk.services.vehicles.VehicleSearchResult;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

import java.util.Arrays;

/** Finds how far some vehicles are from a location with {@link VehiclesManagerV5#searchVehicles}. */
public class SearchVehiclesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleSearchRequest request = new VehicleSearchRequest()
                    .setVehicleIds(Arrays.asList(args.length > 0 ? args : new String[] {"VEHICLE_ID_HERE"}))
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
