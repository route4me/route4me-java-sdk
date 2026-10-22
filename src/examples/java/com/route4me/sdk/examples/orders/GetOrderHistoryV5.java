package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderHistoryEntry;
import com.route4me.sdk.services.orders.OrderHistoryResponse;
import com.route4me.sdk.services.orders.OrderManagerV5;

/**
 * Prints the history of an order with {@link OrderManagerV5#getOrderHistory(Long, String, String)}. Set
 * {@code ORDER_UUID} to the UUID of one of your orders.
 */
public class GetOrderHistoryV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            OrderHistoryResponse history = manager.getOrderHistory(null, System.getenv("ORDER_UUID"), null);
            if (history.getResults() != null) {
                for (OrderHistoryEntry entry : history.getResults()) {
                    System.out.println(entry.getCreatedTimestamp() + ": event " + entry.getEventType());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
