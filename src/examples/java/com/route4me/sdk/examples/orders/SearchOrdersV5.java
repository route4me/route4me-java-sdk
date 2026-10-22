package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.OrderV5;
import com.route4me.sdk.services.orders.SearchOrdersRequest;
import com.route4me.sdk.services.orders.SearchOrdersResponse;

/** Searches orders through the V5 order platform with {@link OrderManagerV5#searchOrders}. */
public class SearchOrdersV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            SearchOrdersRequest request = new SearchOrdersRequest();
            request.setPage(1);
            request.setPerPage(10);
            SearchOrdersResponse response = manager.searchOrders(request);
            if (response.getItems() != null) {
                for (OrderV5 order : response.getItems()) {
                    System.out.println("Order: " + order.getAddressAlias() + " UUID: " + order.getOrderUuid());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
