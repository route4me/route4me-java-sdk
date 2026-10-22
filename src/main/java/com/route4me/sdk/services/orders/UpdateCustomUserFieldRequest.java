package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UpdateCustomUserFieldRequest {
    @SerializedName("order_custom_field_label")
    private String orderCustomFieldLabel;

    @SerializedName("order_custom_field_type")
    private String orderCustomFieldType;

    @SerializedName("order_custom_field_type_info")
    private Object orderCustomFieldTypeInfo;
}
