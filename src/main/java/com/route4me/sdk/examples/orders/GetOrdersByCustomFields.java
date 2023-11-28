// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderRequest;
import com.route4me.sdk.services.orders.OrdersManager;

import java.util.List;

public class GetOrdersByCustomFields {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            List<List<Long>> orders = manager.getOrdersByCustomFields(new OrderRequest().setFields("member_id,order_id"));
            System.out.println(orders);
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
// codebeat:enable[SIMILARITY]
