package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** A category of columns of the destination list. */
@Data
public class RouteDestinationColumnCategory {
    /** Display name of the category (e.g., "Destination Info") ({@code category}). */
    @SerializedName("category")
    private String category;

    /** Columns belonging to this category ({@code columns}). */
    @SerializedName("columns")
    private List<RouteDestinationColumnItem> columns;
}
