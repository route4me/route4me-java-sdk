package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The body of {@link RouteStatusManager#setRouteStopStatus(long[], String)}. */
@Data
public class SetRouteStopStatusRequest {
    /** The IDs of the route destinations ({@code destination_ids}). */
    @SerializedName("destination_ids")
    private long[] destinationIds;

    /** The status to give the destinations; one of the statuses the account allows ({@code status}). */
    @SerializedName("status")
    private String status;
}
