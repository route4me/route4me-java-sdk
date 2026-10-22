package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.BatchUpdateOrdersRequest;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.OrderV5;
import java.util.Arrays;

/**
 * Gives two orders the same group through the V5 order platform with {@link OrderManagerV5#batchUpdateOrders}. Set
 * {@code ORDER_ID_1} and {@code ORDER_ID_2} to the IDs of two of your orders.
 */
public class BatchUpdateOrdersV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        OrderV5 values = new OrderV5();
        values.setFirstName("Alex");
        BatchUpdateOrdersRequest request = new BatchUpdateOrdersRequest();
        request.setOrderIds(Arrays.asList(System.getenv("ORDER_ID_1"), System.getenv("ORDER_ID_2")));
        request.setData(values);
        try {
            OrderV5[] updated = manager.batchUpdateOrders(request);
            System.out.println("Updated orders: " + (updated == null ? 0 : updated.length));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
