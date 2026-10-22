package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UpdateRouteStatusAsPlannedRequest {
    @SerializedName("route_ids")
    private String[] routeIds;
}
