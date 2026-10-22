package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CustomUserField {
    @SerializedName("order_custom_field_uuid")
    private String orderCustomFieldUuid;

    @SerializedName("order_custom_field_name")
    private String orderCustomFieldName;

    @SerializedName("order_custom_field_label")
    private String orderCustomFieldLabel;

    @SerializedName("order_custom_field_type")
    private String orderCustomFieldType;

    @SerializedName("order_custom_field_type_info")
    private Object orderCustomFieldTypeInfo;
}
