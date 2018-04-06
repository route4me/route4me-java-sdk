package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteDeletedResponse {
    @SerializedName("deleted")
    private boolean deleted;

    @SerializedName("async")
    private boolean async;

    @SerializedName("route_id")
    private String routeId;

    @SerializedName("route_ids")
    private List<String> addresses;
}
