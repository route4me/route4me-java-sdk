package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.OrderStatus;
import com.route4me.sdk.services.orders.OrdersGroup;
import com.route4me.sdk.services.orders.OrdersGroupResponse;
import java.util.Arrays;
import java.util.Collections;

/**
 * Lists the groups of the orders scheduled from 2026-10-01 to 2026-10-10 through the V5 API with
 * {@link OrderManagerV5#getOrdersGroup}.
 */
public class GetOrdersGroupV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            OrdersGroupResponse response = manager.getOrdersGroup(Arrays.asList("2026-10-01", "2026-10-10"),
                    Collections.singletonList(OrderStatus.SCHEDULED.getValue()));
            if (response.getData() != null && response.getData().getItems() != null) {
                for (OrdersGroup group : response.getData().getItems()) {
                    System.out.println("Group " + group.getGroupId() + " (" + group.getGroupColumn() + " = "
                            + group.getGroupValue() + "): " + group.getOrdersCount() + " orders");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
