// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;

import java.util.List;

/**
 * Finds the orders with a custom field value with {@link OrdersManager#getOrdersByCustomField}. Set
 * {@code ORDER_CUSTOM_FIELD_KEY} and {@code ORDER_CUSTOM_FIELD_VALUE} to a custom data key of your orders and the value
 * to match.
 */
public class GetOrdersByCustomFieldValue {

    public static void main(String[] args) {
        String key = System.getenv("ORDER_CUSTOM_FIELD_KEY");
        String value = System.getenv("ORDER_CUSTOM_FIELD_VALUE");
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            System.err.println("Set ORDER_CUSTOM_FIELD_KEY and ORDER_CUSTOM_FIELD_VALUE to a custom data key of your"
                    + " orders and the value to match; this example lists the matching orders.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            List<Order> orders = manager.getOrdersByCustomField(key, value);
            if (orders == null) {
                System.out.println("No order has " + key + " = " + value);
                return;
            }
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
