package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link OrderManagerV5#archiveOrders(ArchiveOrdersRequest)}: one page of archived orders. */
@Data
public class ArchiveOrdersResponse {
    /** The archived orders of this page ({@code items}). */
    @SerializedName("items")
    private List<Order> items;

    /** The cursor of the next page, for {@link ArchiveOrdersRequest#setCursor}; none on the last page. */
    @SerializedName("next_page_cursor")
    private String nextPageCursor;
}
