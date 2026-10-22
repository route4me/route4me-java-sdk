package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A territory, with the number of locations in it. */
@Data
public class LocationTerritoryResource {
    /** Unique identifier for the territory ({@code territory_id}). */
    @SerializedName("territory_id")
    private String territoryId;

    /** Display name of the territory ({@code territory_name}). */
    @SerializedName("territory_name")
    private String territoryName;

    /** Color code for territory display (hex format) ({@code territory_color}). */
    @SerializedName("territory_color")
    private String territoryColor;

    /** Member ID associated with the territory ({@code member_id}). */
    @SerializedName("member_id")
    private Integer memberId;

    /** Number of locations within this territory ({@code locations_count}). */
    @SerializedName("locations_count")
    private Integer locationsCount;
}
