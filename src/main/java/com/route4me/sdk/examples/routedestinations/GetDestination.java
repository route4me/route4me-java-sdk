package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.RouteDestinationResource;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;

public class GetDestination {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        try {
            String destinationId = "DESTINATION_ID_HERE";
            RouteDestinationResource dest = manager.getDestination(destinationId);
            System.out.println("Destination: " + dest.getDestinationName());
            System.out.println("Stop Type: " + dest.getAddressStopType());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
