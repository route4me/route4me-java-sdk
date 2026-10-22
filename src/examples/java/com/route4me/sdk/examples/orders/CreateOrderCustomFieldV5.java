package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.CreateCustomUserFieldRequest;
import com.route4me.sdk.services.orders.OrderCustomFieldResource;
import com.route4me.sdk.services.orders.OrderManagerV5;

/**
 * Creates a text order custom field of {@code /orders/custom-fields} with
 * {@link OrderManagerV5#createOrderCustomField}.
 */
public class CreateOrderCustomFieldV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        CreateCustomUserFieldRequest request = new CreateCustomUserFieldRequest();
        request.setOrderCustomFieldName("gate_code");
        request.setOrderCustomFieldLabel("Gate code");
        request.setOrderCustomFieldType("text");
        try {
            OrderCustomFieldResource created = manager.createOrderCustomField(request);
            System.out.println("Created field: " + created.getData().getOrderCustomFieldId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
