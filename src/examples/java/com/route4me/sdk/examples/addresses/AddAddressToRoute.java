package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.Collections;
import java.util.List;

/**
 * Adds a stop to a route with {@link RoutingManager#addAddressesToRoute}. Set {@code ROUTE_ID} to a route of a test
 * account: the example adds one stop to it.
 */
public class AddAddressToRoute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        if (routeId == null || routeId.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example adds a stop to it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            List<Address> addresses = Collections.singletonList(
                    new Address("110 W End Ave New York, NY 10023", 40.7753077, -73.9861529));
            DataObject obj = manager.addAddressesToRoute(routeId, addresses);
            System.out.println(obj);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
