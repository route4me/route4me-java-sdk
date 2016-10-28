package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;

public class AddOrder {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            Order order = new Order();
            order.setAddress1("Some address");
            order.setCachedLatitude(48.335991);
            order.setCachedLongitude(31.18287);
            manager.addOrder(order);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
