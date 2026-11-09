package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.RouteDestinationFieldItem;
import com.route4me.sdk.services.routedestinations.RouteDestinationFieldsResponse;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;

/**
 * Lists the fields the destination list can filter and sort by, with
 * {@link RouteDestinationsManager#getDestinationFields}.
 */
public class GetDestinationFields {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        try {
            RouteDestinationFieldsResponse response = manager.getDestinationFields();
            if (response.getFields() != null) {
                for (RouteDestinationFieldItem field : response.getFields()) {
                    System.out.println(field.getValue() + ": " + field.getLabel() + " (" + field.getType() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
