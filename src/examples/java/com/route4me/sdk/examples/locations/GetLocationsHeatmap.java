package com.route4me.sdk.examples.locations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.locations.LocationHeatmapCollection;
import com.route4me.sdk.services.locations.LocationHeatmapRequest;
import com.route4me.sdk.services.locations.LocationManager;

/** Gets a heat map of the address book locations with {@link LocationManager#getLocationsHeatmap}. */
public class GetLocationsHeatmap {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        LocationManager manager = new LocationManager(apiKey);
        try {
            LocationHeatmapRequest request = new LocationHeatmapRequest();
            request.setZoom(10);
            LocationHeatmapCollection result = manager.getLocationsHeatmap(request);
            if (result.getData() != null) {
                System.out.println("Heatmap data points: " + result.getData().size());
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
