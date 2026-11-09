package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.DestinationFilters;
import com.route4me.sdk.services.routedestinations.GetDestinationsRequest;
import com.route4me.sdk.services.routedestinations.RouteDestinationListResource;
import com.route4me.sdk.services.routedestinations.RouteDestinationsListResponse;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;

/**
 * Lists route destinations through the V5 API with {@link RouteDestinationsManager#getDestinationsList}. Set
 * {@code ROUTE_ID} to one of your routes.
 */
public class GetDestinationsList {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of one of your routes; this example lists its destinations.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        try {
            GetDestinationsRequest request = new GetDestinationsRequest();
            request.setPage(1);
            request.setPerPage(10);
            DestinationFilters filters = new DestinationFilters();
            filters.setRouteId(routeId);
            request.setFilters(filters);
            RouteDestinationsListResponse response = manager.getDestinationsList(request);
            if (response.getItems() != null) {
                for (RouteDestinationListResource item : response.getItems()) {
                    System.out.println("Destination: " + item.getDestinationName());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
