package com.route4me.sdk.examples.orders;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.AsyncStatusResponse;
import com.route4me.sdk.services.orders.BatchUpdateFilterOrderRequest;
import com.route4me.sdk.services.orders.OrderFilters;
import com.route4me.sdk.services.orders.OrderManagerV5;
import java.util.Collections;
import java.util.List;

/**
 * Colors orders red, in the background, through the V5 order platform with {@link OrderManagerV5#batchUpdateFilter}.
 * The filters limit the update to the orders named in {@code ORDER_IDS} that are not routed yet. Set
 * {@code ORDER_IDS} to the comma-separated IDs of orders of a test account: the example changes them. Without it, the
 * example stops, since the same filters without the IDs would change every unrouted order of the account.
 */
public class BatchUpdateFilterV5 {
    public static void main(String[] args) {
        List<String> orderIds = ExampleInput.toList(System.getenv("ORDER_IDS"));
        if (orderIds.isEmpty()) {
            System.err.println("Set ORDER_IDS to the comma-separated IDs of orders of a test account; this example"
                    + " colors them red.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        OrderFilters filters = new OrderFilters();
        filters.setOrderIds(orderIds);
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
