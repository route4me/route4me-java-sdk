package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.OrderCustomUserField;
import com.route4me.sdk.services.orders.OrderManagerV5;
import java.util.List;

/** Lists the order custom fields of {@code /orders/custom-fields} with {@link OrderManagerV5#getOrderCustomFields}. */
public class GetOrderCustomFieldsV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            List<OrderCustomUserField> fields = manager.getOrderCustomFields();
            if (fields != null) {
                for (OrderCustomUserField field : fields) {
                    System.out.println("Field " + field.getOrderCustomFieldId() + ": "
                            + field.getOrderCustomFieldName());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
