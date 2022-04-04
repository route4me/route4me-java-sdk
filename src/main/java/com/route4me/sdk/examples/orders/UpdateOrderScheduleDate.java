package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;


public class UpdateOrderScheduleDate {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            Order order = manager.getOrder(1189380);
            order.setDateScheduled("2021-12-13");
            Order updated = manager.updateOrder(order);
            System.out.println(updated);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
