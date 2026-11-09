package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The {@code filters} of {@link ArchiveOrdersRequest}. */
@Data
public class ArchiveOrdersFilters {
    /** Only the orders added on these dates, as {@code YYYY-MM-DD} ({@code day_added}). */
    @SerializedName("day_added")
    private List<String> dayAdded;
}
