package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link RouteStatusManager#setRouteStopStatus(long[], String)}. */
@Data
public class StatusResponse {
    /** Whether the request succeeded ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
