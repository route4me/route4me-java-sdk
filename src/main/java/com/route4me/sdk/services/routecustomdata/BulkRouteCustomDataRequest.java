package com.route4me.sdk.services.routecustomdata;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BulkRouteCustomDataRequest {
    @SerializedName("route_ids")
    private String[] routeIds;
}
