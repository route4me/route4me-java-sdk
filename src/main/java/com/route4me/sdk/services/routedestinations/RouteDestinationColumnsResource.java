package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link RouteDestinationsManager#getDestinationColumns(String)}. */
@Data
public class RouteDestinationColumnsResource {
    /** Grouped column definitions organised by category ({@code categories}). */
    @SerializedName("categories")
    private List<RouteDestinationColumnCategory> categories;

    /** Current column order as an array of column ID strings ({@code order}). */
    @SerializedName("order")
    private List<String> order;

    /** Default column order as an array of column ID strings ({@code default_order}). */
    @SerializedName("default_order")
    private List<String> defaultOrder;
}
