package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderCustomUserField;
import com.route4me.sdk.services.orders.OrdersManager;
import java.util.List;

/** Lists the custom user fields of orders through the V4 API with {@link OrdersManager#getOrderCustomUserFields}. */
public class GetOrderCustomUserFields {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrdersManager manager = new OrdersManager(apiKey);
        try {
            List<OrderCustomUserField> fields = manager.getOrderCustomUserFields();
            if (fields != null) {
                for (OrderCustomUserField field : fields) {
                    System.out.println("Field " + field.getOrderCustomFieldId() + ": "
                            + field.getOrderCustomFieldName() + " (" + field.getOrderCustomFieldType() + ")");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
