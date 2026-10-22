package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The answer of {@link OrderManagerV5#createOrderCustomField} and {@link OrderManagerV5#updateOrderCustomField}: an
 * order custom field of {@code /modules/api/v5.0/orders/custom-fields}.
 */
@Data
public class OrderCustomFieldResource {
    /** Whether the request succeeded ({@code status}). */
    @SerializedName("status")
    private Boolean status;

    /** The HTTP status of the answer ({@code code}). */
    @SerializedName("code")
    private Integer code;

    /** The custom field ({@code data}). */
    @SerializedName("data")
    private OrderCustomUserField data;
}
