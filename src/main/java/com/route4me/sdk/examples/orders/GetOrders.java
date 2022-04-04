package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrderRequest;
import com.route4me.sdk.services.orders.OrdersManager;

import java.util.List;

public class GetOrders {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            List<Order> orders = manager.getOrders(new OrderRequest());
            System.out.println(orders);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
