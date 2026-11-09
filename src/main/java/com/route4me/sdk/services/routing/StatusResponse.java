package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The status answer of several {@link RoutingManager} methods, such as
 * {@link RoutingManager#routeSharing(String, String)} and
 * {@link RoutingManager#markAddressDeparted(String, Number, boolean, Long, Long, Double, Double)}.
 */
@Data
public class StatusResponse {
    /** Whether the request succeeded ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
