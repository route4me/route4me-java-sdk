package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoutesRequest {
    @SerializedName("route_id")
    private String id;
    @SerializedName("directions")
    private boolean directions;
    @SerializedName("device_tracking_history")
    private boolean deviceTrackingHistory;
    @SerializedName("limit")
    private Integer limit;
    @SerializedName("offset")
    private Integer offset;
    @SerializedName("original")
    private boolean original;
    @SerializedName("notes")
    private boolean notes;
    @SerializedName("query")
    private String query;
    @SerializedName("reoptimize")
    private boolean reoptimize;
    @SerializedName("recompute_directions")
    private boolean recomputeDirections;
}
