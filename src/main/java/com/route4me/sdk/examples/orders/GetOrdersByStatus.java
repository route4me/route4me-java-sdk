// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrderStatus;
import com.route4me.sdk.services.orders.OrdersManager;
import java.util.ArrayList;

import java.util.List;

public class GetOrdersByStatus {
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";

        OrdersManager manager = new OrdersManager(apiKey);
        List<Integer> orderStatus = new ArrayList<>();
        orderStatus.add(OrderStatus.DONE.getValue());
        orderStatus.add(OrderStatus.CANCELLED.getValue());
        orderStatus.add(OrderStatus.DAMAGED.getValue());
        
        try {
            List<Order> orders = manager.getOrdersByStatus(orderStatus);
            for (Order order : orders) {
                System.out.println("OrderID: " + order.getId());
                System.out.println("\tStatus: " + OrderStatus.get(order.getLastStatus()));
                System.out.println("\tCreated: " + order.getCreated());
                System.out.println("\tScheduled: " + order.getDateScheduled());
                System.out.println("\tAddress: " + order.getAddress1());
                System.out.println("\tAlias: " + order.getAddressAlias());
                System.out.println("");
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
// codebeat:enable[SIMILARITY]
