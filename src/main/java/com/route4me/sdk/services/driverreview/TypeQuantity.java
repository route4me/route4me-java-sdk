package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The number of reviews with one rating, in {@link DriverReviewsResponse}. */
@Data
public class TypeQuantity {
    /** The rating ({@code type}). */
    @SerializedName("type")
    private Integer type;

    /** The number of reviews with that rating ({@code quantity}). */
    @SerializedName("quantity")
    private Integer quantity;
}
