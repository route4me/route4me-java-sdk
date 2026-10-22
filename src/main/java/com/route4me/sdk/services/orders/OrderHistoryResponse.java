package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link OrderManagerV5#getOrderHistory(String, String)}. */
@Data
public class OrderHistoryResponse {
    /** The history entries of the order ({@code data}). */
    @SerializedName("data")
    private List<Object> data;
}
