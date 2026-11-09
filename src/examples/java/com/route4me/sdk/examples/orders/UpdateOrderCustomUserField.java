package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderCustomUserField;
import com.route4me.sdk.services.orders.OrderCustomUserFieldResponse;
import com.route4me.sdk.services.orders.OrdersManager;

/**
 * Renames a custom user field of orders through the V4 API with {@link OrdersManager#updateOrderCustomUserField}.
 * Set {@code ORDER_CUSTOM_FIELD_ID} to the ID of a field of a test account: the example changes it.
 */
public class UpdateOrderCustomUserField {
    public static void main(String[] args) {
        String fieldId = System.getenv("ORDER_CUSTOM_FIELD_ID");
        if (fieldId == null || fieldId.isEmpty()) {
            System.err.println("Set ORDER_CUSTOM_FIELD_ID to the ID of an order custom field of a test account; this"
                    + " example changes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        OrderCustomUserField field = new OrderCustomUserField();
        field.setOrderCustomFieldId(Long.parseLong(fieldId));
        field.setOrderCustomFieldLabel("Fragile goods");
        field.setOrderCustomFieldType("checkbox");
        try {
            OrderCustomUserFieldResponse response = manager.updateOrderCustomUserField(field);
            System.out.println("Updated fields: " + response.getAffected());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
