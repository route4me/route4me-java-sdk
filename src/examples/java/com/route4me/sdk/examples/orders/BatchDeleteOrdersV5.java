package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.BatchDeleteOrdersRequest;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.StatusResponse;
import java.util.ArrayList;
import java.util.List;

/** Deletes several orders through the V5 order platform with {@link OrderManagerV5#batchDeleteOrders}. */
public class BatchDeleteOrdersV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            BatchDeleteOrdersRequest request = new BatchDeleteOrdersRequest();
            List<String> orderIds = new ArrayList<String>();
            orderIds.add("ORDER_ID_1");
            orderIds.add("ORDER_ID_2");
            request.setOrderIds(orderIds);
            StatusResponse response = manager.batchDeleteOrders(request);
            System.out.println("Deleted: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
