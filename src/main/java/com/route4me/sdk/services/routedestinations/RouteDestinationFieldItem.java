package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A field the destination list can filter and sort by. */
@Data
public class RouteDestinationFieldItem {
    /** Human-readable label for the field (e.g., "destination uuid") ({@code label}). */
    @SerializedName("label")
    private String label;

    /** Machine-readable field name used in filter/sort requests (e.g., "destination_uuid") ({@code value}). */
    @SerializedName("value")
    private String value;

    /** Field kind: "static" for built-in fields, "custom" for user-defined fields ({@code kind}). */
    @SerializedName("kind")
    private String kind;

    /** Data type of the field (e.g., "string", "integer", "date") ({@code type}). */
    @SerializedName("type")
    private String type;
}
