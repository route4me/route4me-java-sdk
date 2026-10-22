package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link RouteStatusManager#updateRouteStatusAsPlanned(String[])}. */
@Data
public class UpdateRouteStatusAsPlannedResponse {
    /** The new status of each route ({@code data}). */
    @SerializedName("data")
    private List<RouteStatusResponse> data;
}
