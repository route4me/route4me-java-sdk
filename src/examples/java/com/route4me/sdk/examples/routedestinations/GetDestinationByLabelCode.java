package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;
import com.route4me.sdk.services.routedestinations.SortingFilteringResource;

/** Finds the destination of a label code with {@link RouteDestinationsManager#getDestinationByLabelCode}. */
public class GetDestinationByLabelCode {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        try {
            String labelCode = "LABEL_CODE_HERE";
            SortingFilteringResource result = manager.getDestinationByLabelCode(labelCode);
            System.out.println("Route: " + result.getRouteName());
            System.out.println("Address: " + result.getDestinationAddress());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
