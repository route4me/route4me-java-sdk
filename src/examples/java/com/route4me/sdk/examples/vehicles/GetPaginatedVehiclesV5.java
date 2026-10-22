package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehiclePaginateRequest;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;
import com.route4me.sdk.services.vehicles.VehiclesPaginatedResponse;

import java.util.Arrays;
import java.util.Collections;

/**
 * Lists the vehicles one page at a time, sorted by alias, with {@link VehiclesManagerV5#getPaginatedVehiclesList}.
 */
public class GetPaginatedVehiclesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehiclePaginateRequest request = new VehiclePaginateRequest().setPage(1).setPerPage(10)
                    .setOrderBy(Collections.singletonList(Arrays.asList("vehicle_alias", "asc")));
            VehiclesPaginatedResponse page = manager.getPaginatedVehiclesList(request);
            System.out.println("Total vehicles: " + page.getTotal());
            if (page.getData() != null) {
                for (VehicleV5 v : page.getData()) {
                    System.out.println("Vehicle: " + v.getVehicleAlias() + " (ID: " + v.getVehicleId() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
