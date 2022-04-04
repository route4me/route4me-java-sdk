package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;

public class AddOrder {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            Order order = new Order();
            order.setAddress1("Some address");
            order.setCachedLatitude(48.335991);
            order.setCachedLongitude(31.18287);

            order = manager.addOrder(order);           
            System.out.println(order);

        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
