package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.CustomUserFieldResponse;
import com.route4me.sdk.services.orders.OrderManagerV5;

/**
 * Deletes a custom user field of the V5 order platform with {@link OrderManagerV5#deleteCustomUserField}. Set
 * {@code ORDER_CUSTOM_FIELD_UUID} to the UUID of the field to delete.
 */
public class DeleteCustomUserFieldV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            CustomUserFieldResponse deleted = manager.deleteCustomUserField(System.getenv("ORDER_CUSTOM_FIELD_UUID"));
            System.out.println("Deleted field: " + deleted.getData().getOrderCustomFieldName());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
