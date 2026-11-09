package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import com.route4me.sdk.services.orders.BatchDeleteOrdersRequest;
import com.route4me.sdk.services.orders.OrderManagerV5;
import java.util.Arrays;

/**
 * Deletes several orders through the V5 order platform with {@link OrderManagerV5#batchDeleteOrders}. Set
 * {@code ORDER_ID_1} and {@code ORDER_ID_2} to the IDs of two orders of a test account: the example deletes them.
 */
public class BatchDeleteOrdersV5 {
    public static void main(String[] args) {
        String firstOrderId = System.getenv("ORDER_ID_1");
        String secondOrderId = System.getenv("ORDER_ID_2");
        if (firstOrderId == null || firstOrderId.isEmpty() || secondOrderId == null || secondOrderId.isEmpty()) {
            System.err.println("Set ORDER_ID_1 and ORDER_ID_2 to the IDs of two orders of a test account; this"
                    + " example deletes them.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            BatchDeleteOrdersRequest request = new BatchDeleteOrdersRequest();
            request.setOrderIds(Arrays.asList(firstOrderId, secondOrderId));
            StatusResponse response = manager.batchDeleteOrders(request);
            System.out.println("Deleted: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
