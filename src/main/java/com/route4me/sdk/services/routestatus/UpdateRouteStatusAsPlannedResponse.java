package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRouteStatusAsPlannedResponse {
    @SerializedName("data")
    private List<RouteStatusResponse> data;
}
