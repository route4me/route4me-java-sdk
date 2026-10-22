package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteStatusHistoryResponse {
    @SerializedName("data")
    private List<RouteStatusHistoryData> data;
}
