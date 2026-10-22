package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link OrderManagerV5#archiveOrders(ArchiveOrdersRequest)}. */
@Data
public class ArchiveOrdersResponse {
    /** Status of the evidence ({@code status}). */
    @SerializedName("status")
    private Boolean status;

    /** Optional JSON-encoded string with additional evidence data. Null clears the stored data ({@code data}). */
    @SerializedName("data")
    private List<OrderV5> data;
}
