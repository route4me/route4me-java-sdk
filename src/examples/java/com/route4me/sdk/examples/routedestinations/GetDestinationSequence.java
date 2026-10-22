package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.DestinationSequenceRequest;
import com.route4me.sdk.services.routedestinations.DestinationSequenceResponse;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;
import com.route4me.sdk.services.routedestinations.SequenceCoordinate;

import java.util.Arrays;

/** Computes the best visit order of three locations with {@link RouteDestinationsManager#getDestinationSequence}. */
public class GetDestinationSequence {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        DestinationSequenceRequest request = new DestinationSequenceRequest();
        request.setCoordinates(Arrays.asList(coordinate(38.141598f, -85.793846f), coordinate(38.202496f, -85.786514f),
                coordinate(38.178844f, -85.774864f)));
        try {
            DestinationSequenceResponse response = manager.getDestinationSequence(request);
            System.out.println("Visit order: " + Arrays.toString(response.getOptimizedSequence()));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    private static SequenceCoordinate coordinate(float lat, float lng) {
        SequenceCoordinate coordinate = new SequenceCoordinate();
        coordinate.setLat(lat);
        coordinate.setLng(lng);
        return coordinate;
    }
}
