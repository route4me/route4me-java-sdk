package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderCustomFieldResource;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.UpdateCustomUserFieldRequest;

/**
 * Renames an order custom field of {@code /orders/custom-fields} with {@link OrderManagerV5#updateOrderCustomField}.
 * Set {@code ORDER_CUSTOM_FIELD_ID} to the ID of one of your fields.
 */
public class UpdateOrderCustomFieldV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        UpdateCustomUserFieldRequest request = new UpdateCustomUserFieldRequest();
        request.setOrderCustomFieldLabel("Gate access code");
        try {
            OrderCustomFieldResource updated = manager.updateOrderCustomField(
                    Long.parseLong(System.getenv("ORDER_CUSTOM_FIELD_ID")), request);
            System.out.println("Updated field: " + updated.getData().getOrderCustomFieldLabel());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
