package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.DestinationFilters;
import com.route4me.sdk.services.routedestinations.GetDestinationsRequest;
import com.route4me.sdk.services.routedestinations.RouteDestinationListResource;
import com.route4me.sdk.services.routedestinations.RouteDestinationsListResponse;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;

/** Lists route destinations through the V5 API with {@link RouteDestinationsManager#getDestinationsList}. */
public class GetDestinationsList {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        try {
            GetDestinationsRequest request = new GetDestinationsRequest();
            request.setPage(1);
            request.setPerPage(10);
            DestinationFilters filters = new DestinationFilters();
            filters.setRouteId("ROUTE_ID_HERE");
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
