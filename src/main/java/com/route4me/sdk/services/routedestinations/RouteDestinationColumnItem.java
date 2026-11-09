package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A column of the destination list. */
@Data
public class RouteDestinationColumnItem {
    /** Machine-readable column identifier (e.g., "destination_alias") ({@code id}). */
    @SerializedName("id")
    private String id;

    /** Human-readable column title (e.g., "Destination Alias") ({@code title}). */
    @SerializedName("title")
    private String title;
}
