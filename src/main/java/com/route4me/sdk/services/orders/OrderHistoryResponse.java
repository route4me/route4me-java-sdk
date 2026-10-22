package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link OrderManagerV5#getOrderHistory(Long, String, String)}. */
@Data
public class OrderHistoryResponse {
    /** The barcode of the order ({@code order_barcode}). */
    @SerializedName("order_barcode")
    private String orderBarcode;

    /** The history events of the order ({@code results}). */
    @SerializedName("results")
    private List<OrderHistoryEntry> results;

    /** The cursor of the next page, if there is one ({@code next_page_cursor}). */
    @SerializedName("next_page_cursor")
    private String nextPageCursor;
}
