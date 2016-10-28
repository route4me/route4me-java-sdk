package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrderRequest;
import com.route4me.sdk.services.orders.OrdersManager;

import java.util.List;

public class UpdateOrder {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            List<Order> orders = manager.getOrders(new OrderRequest().setQuery("Arthur Kill Rd & Clay Pit Rd"));
            Order old =orders.get(0);
            old.setColor("#333333");
            Order updated = manager.updateOrder(old);
            System.out.println(updated);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
