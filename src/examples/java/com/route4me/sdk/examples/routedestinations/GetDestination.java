package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.RouteDestinationResource;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;

/**
 * Gets one route destination through the V5 API with {@link RouteDestinationsManager#getDestination}. Set
 * {@code ROUTE_DESTINATION_ID} to a destination of one of your routes.
 */
public class GetDestination {
    public static void main(String[] args) {
        String destinationId = System.getenv("ROUTE_DESTINATION_ID");
        if (destinationId == null || destinationId.isEmpty()) {
            System.err.println("Set ROUTE_DESTINATION_ID to a destination of one of your routes; this example reads"
                    + " it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        try {
            RouteDestinationResource dest = manager.getDestination(destinationId);
            System.out.println("Destination: " + dest.getDestinationName());
            System.out.println("Stop Type: " + dest.getAddressStopType());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
