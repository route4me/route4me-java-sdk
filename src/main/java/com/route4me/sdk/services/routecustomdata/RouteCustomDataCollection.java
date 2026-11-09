package com.route4me.sdk.services.routecustomdata;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

/** The answer of {@link RouteCustomDataManager#getBulkRouteCustomData(String[])}: the custom data of each route. */
@Data
public class RouteCustomDataCollection {
    /** The custom data of each route: route ID to its key-value pairs ({@code data}). */
    @SerializedName("data")
    private Map<String, Map<String, String>> data;
}
