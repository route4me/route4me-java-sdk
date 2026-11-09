package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.ArchiveOrdersRequest;
import com.route4me.sdk.services.orders.ArchiveOrdersResponse;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrderManagerV5;

/** Lists the archived orders, page by page, with {@link OrderManagerV5#archiveOrders}. */
public class ArchiveOrdersV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        ArchiveOrdersRequest request = new ArchiveOrdersRequest();
        request.setPerPage(50);
        try {
            do {
                ArchiveOrdersResponse page = manager.archiveOrders(request);
                if (page.getItems() != null) {
                    for (Order order : page.getItems()) {
                        System.out.println("Archived order " + order.getId() + ": " + order.getAddress1());
                    }
                }
                request.setCursor(page.getNextPageCursor());
            } while (request.getCursor() != null);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
