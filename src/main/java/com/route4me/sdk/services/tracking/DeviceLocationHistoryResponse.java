package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** The answer of {@link TrackingManager#getDeviceLocationHistory(DeviceLocationHistoryRequest)}. */
@Data
public class DeviceLocationHistoryResponse {
    /** The recorded positions ({@code data}). */
    @SerializedName("data")
    private List<TrackingHistory> data;
    /** The map-matching data of the positions: matchings, trace points, gaps and a summary ({@code mmd}). */
    @SerializedName("mmd")
    private Map<String, Object> mmd;
}
