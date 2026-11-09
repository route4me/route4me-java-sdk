package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderCustomUserFieldResponse;
import com.route4me.sdk.services.orders.OrdersManager;

/**
 * Removes a custom user field of orders through the V4 API with {@link OrdersManager#removeOrderCustomUserField}.
 * Set {@code ORDER_CUSTOM_FIELD_ID} to the ID of a field of a test account: the example removes it.
 */
public class RemoveOrderCustomUserField {
    public static void main(String[] args) {
        String fieldId = System.getenv("ORDER_CUSTOM_FIELD_ID");
        if (fieldId == null || fieldId.isEmpty()) {
            System.err.println("Set ORDER_CUSTOM_FIELD_ID to the ID of an order custom field of a test account; this"
                    + " example removes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            OrderCustomUserFieldResponse response = manager.removeOrderCustomUserField(Long.parseLong(fieldId));
            System.out.println("Removed fields: " + response.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
