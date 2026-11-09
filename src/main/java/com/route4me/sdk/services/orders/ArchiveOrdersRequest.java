package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of {@link OrderManagerV5#archiveOrders(ArchiveOrdersRequest)}; fields left {@code null} are not sent. */
@Data
public class ArchiveOrdersRequest {
    /** The page to read: the {@code next_page_cursor} of the previous answer; none for the first ({@code cursor}). */
    @SerializedName("cursor")
    private String cursor;

    /** The number of orders per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;

    /** The filters ({@code filters}). */
    @SerializedName("filters")
    private ArchiveOrdersFilters filters;
}
