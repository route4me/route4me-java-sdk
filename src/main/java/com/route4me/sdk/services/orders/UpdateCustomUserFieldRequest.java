package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of {@link OrderManagerV5#updateCustomUserField(String, UpdateCustomUserFieldRequest)}. */
@Data
public class UpdateCustomUserFieldRequest {
    /** Updated label for the custom field ({@code order_custom_field_label}). */
    @SerializedName("order_custom_field_label")
    private String orderCustomFieldLabel;

    /** Updated type of the custom field ({@code order_custom_field_type}). */
    @SerializedName("order_custom_field_type")
    private String orderCustomFieldType;

    /** Custom order field type info ({@code order_custom_field_type_info}). */
    @SerializedName("order_custom_field_type_info")
    private Object orderCustomFieldTypeInfo;
}
