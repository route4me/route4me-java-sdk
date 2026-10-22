package com.route4me.sdk.services.routecustomdata;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

@Data
public class RouteCustomDataCollection {
    @SerializedName("data")
    private Map<String, Map<String, String>> data;
}
