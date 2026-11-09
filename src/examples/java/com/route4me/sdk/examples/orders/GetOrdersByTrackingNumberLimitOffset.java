// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.orders;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrderStatus;
import com.route4me.sdk.services.orders.OrdersManager;

import java.util.List;

/**
 * Finds orders by tracking number, one page at a time, with {@link OrdersManager#getOrdersByTrackingNumber}. Set
 * {@code TRACKING_NUMBERS} to the comma-separated tracking numbers of some of your orders.
 */
public class GetOrdersByTrackingNumberLimitOffset {
    public static void main(String[] args) {
        List<String> trackingNumbers = ExampleInput.toList("TRACKING_NUMBERS", System.getenv("TRACKING_NUMBERS"));
        if (trackingNumbers == null) {
            return;
        }
        if (trackingNumbers.isEmpty()) {
            System.err.println("Set TRACKING_NUMBERS to the comma-separated tracking numbers of some of your orders;"
                    + " this example finds those orders.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);

        try {
            List<Order> orders = manager.getOrdersByTrackingNumber(trackingNumbers, 100, 0);
            if (orders == null) {
                System.out.println("No order has those tracking numbers");
                return;
            }
            System.out.println("Found " + orders.size() + " Orders");
            for (Order order : orders) {
                System.out.println("OrderID: " + order.getId());
                System.out.println("\tStatus: " + OrderStatus.get(order.getLastStatus()));
                System.out.println("\tTracking Number: " + order.getTrackingNumber());
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
