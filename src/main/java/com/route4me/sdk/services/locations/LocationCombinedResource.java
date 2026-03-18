package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LocationCombinedResource {
    @SerializedName("data")
    private LocationCombinedData data;

    @SerializedName("config")
    private LocationConfig config;
}
