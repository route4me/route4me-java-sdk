package com.route4me.sdk.services.routestatus;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SetRouteStopStatusRequest {
    @SerializedName("destination_ids")
    private long[] destinationIds;

    @SerializedName("status")
    private String status;
}
