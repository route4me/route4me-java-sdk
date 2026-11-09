package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link RouteStatusManager#getRouteStatusHistory(String, String, Long, Long)}. */
@Data
public class RouteStatusHistoryResponse {
    /** The status changes of the route ({@code data}). */
    @SerializedName("data")
    private List<RouteStatusHistoryData> data;
}
