package com.route4me.sdk.examples.addresses;

import com.google.gson.JsonElement;
import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;
import java.util.List;

/**
 * Deletes stops from their routes with {@link RoutingManager#deleteRouteDestinations}. Set
 * {@code ROUTE_DESTINATION_IDS} to the comma-separated IDs of stops of a test account: the example deletes them.
 */
public class DeleteRouteDestinations {

    public static void main(String[] args) {
        List<Long> ids = ExampleInput.toLongs("ROUTE_DESTINATION_IDS", System.getenv("ROUTE_DESTINATION_IDS"));
        if (ids == null) {
            return;
        }
        if (ids.isEmpty()) {
            System.err.println("Set ROUTE_DESTINATION_IDS to the comma-separated IDs of stops of a test account;"
                    + " this example deletes them.");
            return;
        }
        long[] routeDestinationIds = new long[ids.size()];
        for (int i = 0; i < routeDestinationIds.length; i++) {
            routeDestinationIds[i] = ids.get(i);
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            JsonElement result = manager.deleteRouteDestinations(routeDestinationIds);
            System.out.println(result);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
