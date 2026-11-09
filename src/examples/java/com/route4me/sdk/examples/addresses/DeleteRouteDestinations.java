package com.route4me.sdk.examples.addresses;

import com.google.gson.JsonElement;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Deletes stops from their routes with {@link RoutingManager#deleteRouteDestinations}. Set
 * {@code ROUTE_DESTINATION_IDS} to the comma-separated IDs of stops of a test account: the example deletes them.
 */
public class DeleteRouteDestinations {

    public static void main(String[] args) {
        String ids = System.getenv("ROUTE_DESTINATION_IDS");
        if (ids == null || ids.trim().isEmpty()) {
            System.err.println("Set ROUTE_DESTINATION_IDS to the comma-separated IDs of stops of a test account;"
                    + " this example deletes them.");
            return;
        }
        String[] parts = ids.trim().split("\\s*,\\s*");
        long[] routeDestinationIds = new long[parts.length];
        for (int i = 0; i < parts.length; i++) {
            routeDestinationIds[i] = Long.parseLong(parts[i]);
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
