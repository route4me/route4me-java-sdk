package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderUpdate;
import com.route4me.sdk.services.orders.OrdersManager;
import com.route4me.sdk.services.orders.OrdersUpdatesResponse;

/** Lists the orders changed in the last 24 hours through the V4 API with {@link OrdersManager#getOrdersUpdates}. */
public class GetOrdersUpdates {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        long oneDayAgo = System.currentTimeMillis() / 1000L - 24L * 60L * 60L;
        try {
            OrdersUpdatesResponse updates = manager.getOrdersUpdates(oneDayAgo);
            if (updates.getData() != null) {
                for (OrderUpdate update : updates.getData()) {
                    System.out.println("Order " + update.getOrderId() + " changed at " + update.getUpdatedTimestamp());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
