package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;
import com.route4me.sdk.services.routedestinations.SortingFilteringResource;

/**
 * Finds the destination of a label code with {@link RouteDestinationsManager#getDestinationByLabelCode}. Set
 * {@code LABEL_CODE} to the label code of a destination of one of your routes.
 */
public class GetDestinationByLabelCode {
    public static void main(String[] args) {
        String labelCode = System.getenv("LABEL_CODE");
        if (labelCode == null || labelCode.isEmpty()) {
            System.err.println("Set LABEL_CODE to the label code of a destination of one of your routes; this example"
                    + " finds the destination.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        try {
            SortingFilteringResource result = manager.getDestinationByLabelCode(labelCode);
            System.out.println("Route: " + result.getRouteName());
            System.out.println("Address: " + result.getDestinationAddress());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
