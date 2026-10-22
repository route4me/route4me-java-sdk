package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderCustomFieldTypeInfo;
import com.route4me.sdk.services.orders.OrderCustomUserField;
import com.route4me.sdk.services.orders.OrderCustomUserFieldResponse;
import com.route4me.sdk.services.orders.OrdersManager;

/**
 * Creates a checkbox custom user field of orders through the V4 API with
 * {@link OrdersManager#createOrderCustomUserField}.
 */
public class CreateOrderCustomUserField {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        OrderCustomFieldTypeInfo info = new OrderCustomFieldTypeInfo();
        info.setShortLabel("Fragile");
        OrderCustomUserField field = new OrderCustomUserField();
        field.setOrderCustomFieldName("is_fragile");
        field.setOrderCustomFieldLabel("Fragile");
        field.setOrderCustomFieldType("checkbox");
        field.setOrderCustomFieldTypeInfo(info);
        try {
            OrderCustomUserFieldResponse response = manager.createOrderCustomUserField(field);
            System.out.println("Created field: " + response.getData().getOrderCustomFieldId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
