package com.route4me.sdk.examples.orders;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;

/** Gets one order with {@link OrdersManager#getOrder}. Set {@code ORDER_ID} to the ID of one of your orders. */
public class GetOrderById {
    public static void main(String[] args) {
        String orderId = System.getenv("ORDER_ID");
        if (orderId == null || orderId.isEmpty()) {
            System.err.println("Set ORDER_ID to the ID of one of your orders; this example reads it.");
            return;
        }
        Long id = ExampleInput.toLong("ORDER_ID", orderId);
        if (id == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            Order order = manager.getOrder(id);
            System.out.println(order);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
