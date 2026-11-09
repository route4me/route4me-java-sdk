package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import com.route4me.sdk.services.orders.OrderManagerV5;

/**
 * Deletes an order custom field of {@code /orders/custom-fields} with {@link OrderManagerV5#deleteOrderCustomField}.
 * Set {@code ORDER_CUSTOM_FIELD_ID} to the ID of a field of a test account: the example deletes it.
 */
public class DeleteOrderCustomFieldV5 {
    public static void main(String[] args) {
        String fieldId = System.getenv("ORDER_CUSTOM_FIELD_ID");
        if (fieldId == null || fieldId.isEmpty()) {
            System.err.println("Set ORDER_CUSTOM_FIELD_ID to the ID of an order custom field of a test account; this"
                    + " example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            StatusResponse response = manager.deleteOrderCustomField(Long.parseLong(fieldId));
            System.out.println("Deleted: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
