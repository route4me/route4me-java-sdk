package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.RoutingManager;

/** Marks a stop of a route as visited, now and here, with {@link RoutingManager#markAddressVisited}. */
public class MarkAddressVisited {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            long now = System.currentTimeMillis() / 1000L;
            Integer marked = manager.markAddressVisited("ROUTE_ID_HERE", 123456789L, true, null, now, 38.141598,
                    -85.793846);
            System.out.println("Marked as visited: " + marked);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
