package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.CustomUserField;
import com.route4me.sdk.services.orders.CustomUserFieldsResponse;
import com.route4me.sdk.services.orders.OrderManagerV5;

/** Lists the custom user fields of the V5 order platform with {@link OrderManagerV5#getCustomUserFields}. */
public class GetCustomUserFieldsV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            CustomUserFieldsResponse response = manager.getCustomUserFields();
            if (response.getData() != null) {
                for (CustomUserField field : response.getData()) {
                    System.out.println("Field " + field.getOrderCustomFieldUuid() + ": "
                            + field.getOrderCustomFieldName());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
