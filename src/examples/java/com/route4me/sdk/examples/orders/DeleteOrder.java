package com.route4me.sdk.examples.orders;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import com.route4me.sdk.services.orders.OrdersManager;

/**
 * Deletes one order with {@link OrdersManager#deleteOrders}. Set {@code ORDER_ID} to an order of a test account: the
 * example deletes it.
 */
public class DeleteOrder {
    public static void main(String[] args) {
        String orderId = System.getenv("ORDER_ID");
        if (orderId == null || orderId.isEmpty()) {
            System.err.println("Set ORDER_ID to the ID of an order of a test account; this example deletes it.");
            return;
        }
        Long id = ExampleInput.toLong("ORDER_ID", orderId);
        if (id == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            StatusResponse resp = manager.deleteOrders(id);
            System.out.println(resp);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
