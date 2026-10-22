package com.route4me.sdk.examples.addresses;

import com.google.gson.JsonElement;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;

/** Deletes two stops from their routes with {@link RoutingManager#deleteRouteDestinations}. */
public class DeleteRouteDestinations {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            JsonElement result = manager.deleteRouteDestinations(123456789L, 123456790L);
            System.out.println(result);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
