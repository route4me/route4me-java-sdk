package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderFilters;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.OrderV5;
import com.route4me.sdk.services.orders.SearchOrdersRequest;
import com.route4me.sdk.services.orders.SearchOrdersResponse;

/**
 * Searches the unrouted orders through the V5 order platform, ten at a time, with
 * {@link OrderManagerV5#searchOrders}.
 */
public class SearchOrdersV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            OrderFilters filters = new OrderFilters();
            filters.setDisplay("unrouted");
            SearchOrdersRequest request = new SearchOrdersRequest();
            request.setLimit(10);
            request.setOffset(0);
            request.setFilters(filters);
            SearchOrdersResponse response = manager.searchOrders(request);
            System.out.println("Matching orders: " + response.getTotal());
            if (response.getResults() != null) {
                for (OrderV5 order : response.getResults()) {
                    System.out.println("Order: " + order.getAddressAlias() + " UUID: " + order.getOrderUuid());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
