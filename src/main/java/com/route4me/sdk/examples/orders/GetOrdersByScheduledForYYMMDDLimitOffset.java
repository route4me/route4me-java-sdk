// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;
import java.util.Arrays;

import java.util.List;

public class GetOrdersByScheduledForYYMMDDLimitOffset {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            List<String> scheduledDate = Arrays.asList("2021-09-10", "2021-09-10");
            List<Order> orders = manager.getOrdersByScheduledDate(scheduledDate, 10, 0);
            System.out.println("Found " + orders.size() + " Orders");
            for (Order order : orders) {
                System.out.println("OrderID: " + order.getId());
                System.out.println("\tCreated: " + order.getCreated());
                System.out.println("\tScheduled: " + order.getDateScheduled());
                System.out.println("\tAddress: " + order.getAddress1());
                System.out.println("\tAlias: " + order.getAddressAlias());
                System.out.println("\tCustom Data: " + order.getCustomData());
                System.out.println("");
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
// codebeat:enable[SIMILARITY]
