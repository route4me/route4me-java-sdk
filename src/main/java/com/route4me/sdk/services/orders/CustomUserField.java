package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A custom user field of orders. */
@Data
public class CustomUserField {
    /** Custom order field ID ({@code order_custom_field_uuid}). */
    @SerializedName("order_custom_field_uuid")
    private String orderCustomFieldUuid;

    /** Unique name of the custom field ({@code order_custom_field_name}). */
    @SerializedName("order_custom_field_name")
    private String orderCustomFieldName;

    /** Label of the custom field ({@code order_custom_field_label}). */
    @SerializedName("order_custom_field_label")
    private String orderCustomFieldLabel;

    /** Type of the custom field ({@code order_custom_field_type}). */
    @SerializedName("order_custom_field_type")
    private String orderCustomFieldType;

    /** Information about an order's custom field ({@code order_custom_field_type_info}). */
    @SerializedName("order_custom_field_type_info")
    private Object orderCustomFieldTypeInfo;
}
