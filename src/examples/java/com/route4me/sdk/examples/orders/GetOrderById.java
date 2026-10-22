package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;

public class GetOrderById {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            Order order = manager.getOrder(1317);
            System.out.println(order);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
