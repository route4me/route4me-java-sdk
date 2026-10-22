package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.StatusResponse;

/**
 * Deletes an order custom field of {@code /orders/custom-fields} with {@link OrderManagerV5#deleteOrderCustomField}.
 * Set {@code ORDER_CUSTOM_FIELD_ID} to the ID of the field to delete.
 */
public class DeleteOrderCustomFieldV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            StatusResponse response = manager.deleteOrderCustomField(
                    Long.parseLong(System.getenv("ORDER_CUSTOM_FIELD_ID")));
            System.out.println("Deleted: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
