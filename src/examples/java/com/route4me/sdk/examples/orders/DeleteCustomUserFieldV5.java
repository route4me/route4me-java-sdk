package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.CustomUserFieldResponse;
import com.route4me.sdk.services.orders.OrderManagerV5;

/**
 * Deletes a custom user field of the V5 order platform with {@link OrderManagerV5#deleteCustomUserField}. Set
 * {@code ORDER_CUSTOM_FIELD_UUID} to the UUID of a field of a test account: the example deletes it.
 */
public class DeleteCustomUserFieldV5 {
    public static void main(String[] args) {
        String fieldUuid = System.getenv("ORDER_CUSTOM_FIELD_UUID");
        if (fieldUuid == null || fieldUuid.isEmpty()) {
            System.err.println("Set ORDER_CUSTOM_FIELD_UUID to the UUID of an order custom field of a test account;"
                    + " this example deletes it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            CustomUserFieldResponse deleted = manager.deleteCustomUserField(fieldUuid);
            System.out.println("Deleted field: " + deleted.getData().getOrderCustomFieldName());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
