package com.route4me.sdk.services.orders;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Type;
import lombok.Data;

/**
 * A custom user field of orders, as the V4 API ({@code /api.v4/order_custom_user_fields.php}) and the V5
 * {@code /orders/custom-fields} endpoints of {@link OrderManagerV5} take and return it. Fields left {@code null} are
 * not sent.
 */
@Data
public class OrderCustomUserField {

    /** The ID of the custom field ({@code order_custom_field_id}). */
    @SerializedName("order_custom_field_id")
    private Long orderCustomFieldId;
    /** The unique name of the custom field ({@code order_custom_field_name}). */
    @SerializedName("order_custom_field_name")
    private String orderCustomFieldName;
    /** The label of the custom field ({@code order_custom_field_label}). */
    @SerializedName("order_custom_field_label")
    private String orderCustomFieldLabel;
    /** The type of the custom field, such as {@code text}, {@code checkbox} or {@code dropdown}. */
    @SerializedName("order_custom_field_type")
    private String orderCustomFieldType;
    /**
     * The value of the custom field; for a {@code checkbox} field, {@code true} or {@code false} ({@code
     * order_custom_field_value}).
     */
    @SerializedName("order_custom_field_value")
    private String orderCustomFieldValue;
    /** The account owner's member ID ({@code root_owner_member_id}). */
    @SerializedName("root_owner_member_id")
    private Long rootOwnerMemberId;
    /**
     * The labels and allowed values of the custom field ({@code order_custom_field_type_info}). An empty JSON array
     * ({@code []}), which the PHP API sends for an empty map, is read as {@code null}.
     */
    @SerializedName("order_custom_field_type_info")
    @JsonAdapter(TypeInfoReader.class)
    private OrderCustomFieldTypeInfo orderCustomFieldTypeInfo;

    /**
     * Reads {@code order_custom_field_type_info} from an object, and an empty array as {@code null}. A non-empty array
     * is not a type info and still fails. Writing is Gson's own.
     */
    static final class TypeInfoReader implements JsonDeserializer<OrderCustomFieldTypeInfo> {
        @Override
        public OrderCustomFieldTypeInfo deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            if (json.isJsonArray() && json.getAsJsonArray().size() == 0) {
                return null;
            }
            return context.deserialize(json, OrderCustomFieldTypeInfo.class);
        }
    }
}
