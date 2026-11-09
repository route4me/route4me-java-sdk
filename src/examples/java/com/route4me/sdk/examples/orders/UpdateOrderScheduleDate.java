package com.route4me.sdk.examples.orders;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;

import java.time.LocalDate;


/**
 * Reads an order with {@link OrdersManager#getOrder} and moves its scheduled date to tomorrow with
 * {@link OrdersManager#updateOrder}. Set {@code ORDER_ID} to an order of a test account: the example changes it.
 */
public class UpdateOrderScheduleDate {
    public static void main(String[] args) {
        String orderId = System.getenv("ORDER_ID");
        if (orderId == null || orderId.isEmpty()) {
            System.err.println("Set ORDER_ID to the ID of an order of a test account; this example changes it.");
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
            order.setDateScheduled(LocalDate.now().plusDays(1).toString());
            Order updated = manager.updateOrder(order);
            System.out.println(updated);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
