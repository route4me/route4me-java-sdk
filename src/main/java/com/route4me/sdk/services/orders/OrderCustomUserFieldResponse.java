package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of the V4 methods of {@link OrdersManager} that create, update or remove an order custom field. */
@Data
public class OrderCustomUserFieldResponse {
    /** The result, such as {@code OK} ({@code result}). */
    @SerializedName("result")
    private String result;

    /** The number of fields changed ({@code affected}). */
    @SerializedName("affected")
    private Integer affected;

    /** The created or updated field; none when a field is removed ({@code data}). */
    @SerializedName("data")
    private OrderCustomUserField data;
}
