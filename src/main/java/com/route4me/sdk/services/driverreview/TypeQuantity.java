package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TypeQuantity {
    @SerializedName("type")
    private Integer type;

    @SerializedName("quantity")
    private Integer quantity;
}
