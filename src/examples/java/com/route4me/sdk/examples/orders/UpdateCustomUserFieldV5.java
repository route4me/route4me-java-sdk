package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.CustomUserFieldResponse;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.UpdateCustomUserFieldRequest;
import java.util.Collections;

/**
 * Renames a custom user field of the V5 order platform with {@link OrderManagerV5#updateCustomUserField}. Set
 * {@code ORDER_CUSTOM_FIELD_UUID} to the UUID of one of your fields.
 */
public class UpdateCustomUserFieldV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        UpdateCustomUserFieldRequest request = new UpdateCustomUserFieldRequest();
        request.setOrderCustomFieldLabel("Preferred delivery window");
        request.setOrderCustomFieldType("text");
        request.setOrderCustomFieldTypeInfo(Collections.singletonMap("short_label", "Window"));
        try {
            CustomUserFieldResponse updated = manager.updateCustomUserField(System.getenv("ORDER_CUSTOM_FIELD_UUID"),
                    request);
            System.out.println("Updated field: " + updated.getData().getOrderCustomFieldLabel());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
