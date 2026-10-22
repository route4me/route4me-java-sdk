package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.AsyncStatusResponse;
import com.route4me.sdk.services.orders.BatchUpdateFilterOrderRequest;
import com.route4me.sdk.services.orders.OrderFilters;
import com.route4me.sdk.services.orders.OrderManagerV5;
import java.util.Collections;

/**
 * Sets the color of every unrouted order, in the background, through the V5 order platform with
 * {@link OrderManagerV5#batchUpdateFilter}.
 */
public class BatchUpdateFilterV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        OrderFilters filters = new OrderFilters();
        filters.setDisplay("unrouted");
        BatchUpdateFilterOrderRequest request = new BatchUpdateFilterOrderRequest();
        request.setFilters(filters);
        request.setData(Collections.<String, Object>singletonMap("color", "ff0000"));
        try {
            AsyncStatusResponse response = manager.batchUpdateFilter(request);
            System.out.println("Accepted: " + response.getStatus() + ", in the background: " + response.getAsync());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
