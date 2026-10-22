package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link LocationManager#getLocationsCombined(LocationCombinedRequest)}. */
@Data
public class LocationCombinedResource {
    /** Location data with items, pagination, and grouping information ({@code data}). */
    @SerializedName("data")
    private LocationCombinedData data;

    /** Configuration for column display and customization ({@code config}). */
    @SerializedName("config")
    private LocationConfig config;
}
