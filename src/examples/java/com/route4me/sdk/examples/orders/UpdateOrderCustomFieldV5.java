package com.route4me.sdk.examples.orders;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderCustomFieldResource;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.UpdateCustomUserFieldRequest;

/**
 * Renames an order custom field of {@code /orders/custom-fields} with {@link OrderManagerV5#updateOrderCustomField}.
 * Set {@code ORDER_CUSTOM_FIELD_ID} to the ID of a field of a test account: the example renames it.
 */
public class UpdateOrderCustomFieldV5 {
    public static void main(String[] args) {
        String fieldId = System.getenv("ORDER_CUSTOM_FIELD_ID");
        if (fieldId == null || fieldId.isEmpty()) {
            System.err.println("Set ORDER_CUSTOM_FIELD_ID to the ID of an order custom field of a test account; this"
                    + " example renames it.");
            return;
        }
        Long customFieldId = ExampleInput.toLong("ORDER_CUSTOM_FIELD_ID", fieldId);
        if (customFieldId == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        UpdateCustomUserFieldRequest request = new UpdateCustomUserFieldRequest();
        request.setOrderCustomFieldLabel("Gate access code");
        try {
            OrderCustomFieldResource updated = manager.updateOrderCustomField(customFieldId, request);
            System.out.println("Updated field: " + updated.getData().getOrderCustomFieldLabel());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
