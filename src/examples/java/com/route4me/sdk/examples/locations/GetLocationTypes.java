package com.route4me.sdk.examples.locations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.locations.LocationManager;
import com.route4me.sdk.services.locations.LocationTypeCollection;
import com.route4me.sdk.services.locations.LocationTypeFilters;
import com.route4me.sdk.services.locations.LocationTypeResource;

/**
 * Lists the location types that can have child locations, with
 * {@link LocationManager#getLocationTypes(LocationTypeFilters, Integer, Integer)}.
 */
public class GetLocationTypes {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        LocationManager manager = new LocationManager(apiKey);
        try {
            LocationTypeFilters filters = new LocationTypeFilters();
            filters.setIsParentType(true);
            LocationTypeCollection types = manager.getLocationTypes(filters, 1, 20);
            if (types.getData() != null) {
                for (LocationTypeResource type : types.getData()) {
                    System.out.println("Location type: " + type.getName() + " (ID: " + type.getLocationTypeId() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
