package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.AsyncStatusResponse;
import com.route4me.sdk.services.orders.BatchCreateOrdersRequest;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.OrderV5;
import java.util.Arrays;

/** Creates two orders in one request through the V5 order platform with {@link OrderManagerV5#batchCreateOrders}. */
public class BatchCreateOrdersV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        OrderV5 first = new OrderV5();
        first.setAddress1("100 Example St, Springfield, IL");
        first.setAddressAlias("First order");
        OrderV5 second = new OrderV5();
        second.setAddress1("200 Example St, Springfield, IL");
        second.setAddressAlias("Second order");
        BatchCreateOrdersRequest request = new BatchCreateOrdersRequest();
        request.setData(Arrays.asList(first, second));
        try {
            AsyncStatusResponse response = manager.batchCreateOrders(request);
            System.out.println("Accepted: " + response.getStatus() + ", in the background: " + response.getAsync());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
