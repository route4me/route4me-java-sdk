package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;

public class AddOrderWithDemands {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            Order order = new Order();
            order.setAddress1("Some address");
            order.setCachedLatitude(48.335991);
            order.setCachedLongitude(31.18287);
            order.setWeight(500.0);
            order.setCost(100.0);
            order.setRevenue(1500.0);
            order.setCube(2500.0);
            order.setPieces(1500);
            
            order = manager.addOrder(order);           
            System.out.println(order);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
