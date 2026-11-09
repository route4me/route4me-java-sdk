package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of {@link OrderManagerV5#createCustomUserField(CreateCustomUserFieldRequest)}. */
@Data
public class CreateCustomUserFieldRequest {
    /** Unique name of the custom field ({@code order_custom_field_name}). */
    @SerializedName("order_custom_field_name")
    private String orderCustomFieldName;

    /** Label of the custom field ({@code order_custom_field_label}). */
    @SerializedName("order_custom_field_label")
    private String orderCustomFieldLabel;

    /** Type of the custom field ({@code order_custom_field_type}). */
    @SerializedName("order_custom_field_type")
    private String orderCustomFieldType;

    /** Custom order field type info ({@code order_custom_field_type_info}). */
    @SerializedName("order_custom_field_type_info")
    private Object orderCustomFieldTypeInfo;
}
