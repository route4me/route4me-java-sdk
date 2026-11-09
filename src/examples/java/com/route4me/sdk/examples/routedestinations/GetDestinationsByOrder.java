package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.RouteDestinationsByOrderResponse;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;

/**
 * Lists the route destinations of an order with {@link RouteDestinationsManager#getDestinationsByOrder}. Set
 * {@code ORDER_UUID} to the UUID of one of your orders.
 */
public class GetDestinationsByOrder {
    public static void main(String[] args) {
        String orderUuid = System.getenv("ORDER_UUID");
        if (orderUuid == null || orderUuid.isEmpty()) {
            System.err.println("Set ORDER_UUID to the UUID of one of your orders; this example lists its route"
                    + " destinations.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        try {
            RouteDestinationsByOrderResponse response = manager.getDestinationsByOrder(orderUuid);
            System.out.println(response.getItems());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
