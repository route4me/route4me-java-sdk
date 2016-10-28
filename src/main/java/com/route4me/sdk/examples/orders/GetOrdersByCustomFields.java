package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrderRequest;
import com.route4me.sdk.services.orders.OrdersManager;

import java.util.List;

public class GetOrdersByCustomFields {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            List<Order> orders = manager.getOrders(new OrderRequest().setFields("member_id,order_id"));
            System.out.println(orders);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
