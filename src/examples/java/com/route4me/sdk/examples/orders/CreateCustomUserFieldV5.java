package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.CreateCustomUserFieldRequest;
import com.route4me.sdk.services.orders.CustomUserFieldResponse;
import com.route4me.sdk.services.orders.OrderManagerV5;
import java.util.Collections;

/** Creates a custom user field of the V5 order platform with {@link OrderManagerV5#createCustomUserField}. */
public class CreateCustomUserFieldV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        CreateCustomUserFieldRequest request = new CreateCustomUserFieldRequest();
        request.setOrderCustomFieldName("delivery_window");
        request.setOrderCustomFieldLabel("Delivery window");
        request.setOrderCustomFieldType("text");
        request.setOrderCustomFieldTypeInfo(Collections.singletonMap("short_label", "Window"));
        try {
            CustomUserFieldResponse created = manager.createCustomUserField(request);
            System.out.println("Created field: " + created.getData().getOrderCustomFieldUuid());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
