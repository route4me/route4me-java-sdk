package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.RoutingManager;

/**
 * Reads a stop of a route with {@link RoutingManager#getAddress} and changes its alias and custom fields with
 * {@link RoutingManager#updateAddressAttribute}. Set {@code ROUTE_ID} to a route of a test account and
 * {@code ROUTE_DESTINATION_ID} to one of its stops: the example changes that stop.
 */
public class UpdateAddressAttribute {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String routeDestinationId = System.getenv("ROUTE_DESTINATION_ID");
        if (routeId == null || routeId.isEmpty() || routeDestinationId == null || routeDestinationId.isEmpty()) {
            System.err.println("Set ROUTE_ID and ROUTE_DESTINATION_ID to a route of a test account and one of its"
                    + " stops; this example changes the stop.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        try {
            Long destinationId = Long.valueOf(routeDestinationId);
            Address addr = manager.getAddress(routeId, destinationId);
            addr.setAlias("New Alias");
            addr.addCustomField("NOTE 1", "Adding Custom Fields");
            Address address = manager.updateAddressAttribute(routeId, destinationId, addr);
            System.out.println(address);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
