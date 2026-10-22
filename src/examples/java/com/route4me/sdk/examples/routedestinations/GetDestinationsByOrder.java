package com.route4me.sdk.examples.routedestinations;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routedestinations.RouteDestinationsByOrderResponse;
import com.route4me.sdk.services.routedestinations.RouteDestinationsManager;

/** Lists the route destinations of an order with {@link RouteDestinationsManager#getDestinationsByOrder}. */
public class GetDestinationsByOrder {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RouteDestinationsManager manager = new RouteDestinationsManager(apiKey);
        try {
            RouteDestinationsByOrderResponse response = manager.getDestinationsByOrder("ORDER_UUID_HERE");
            System.out.println(response.getItems());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
