package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One changed field of an {@link OrderHistoryEntry}. */
@Data
public class OrderDiff {
    /** The name of the field, such as {@code member_id} ({@code field}). */
    @SerializedName("field")
    private String field;

    /** The value before the change ({@code old_value}). */
    @SerializedName("old_value")
    private Object oldValue;

    /** The value after the change ({@code new_value}). */
    @SerializedName("new_value")
    private Object newValue;
}
