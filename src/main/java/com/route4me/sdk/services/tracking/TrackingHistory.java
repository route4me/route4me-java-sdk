package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One recorded GPS position of a route. */
@Data
public class TrackingHistory {
    /** Speed at the time of the location transaction event ({@code s}). */
    @SerializedName("s")
    private String s;
    /** The unit of the speed, such as {@code mph} or {@code kph} ({@code su}). */
    @SerializedName("su")
    private String speedUnit;
    /** The member ID ({@code m}). */
    @SerializedName("m")
    private String memberId;
    /** The source of the position, such as {@code R4M} ({@code src}). */
    @SerializedName("src")
    private String source;
    /** Latitude at the time of the location transaction event ({@code lt}). */
    @SerializedName("lt")
    private Double latitude;
    /** Longitude at the time of the location transaction event ({@code lg}). */
    @SerializedName("lg")
    private Double longitude;
    /** Direction/heading at the time of the location transaction event ({@code d}). */
    @SerializedName("d")
    private String d;
    /** The original timestamp in unix timestamp format at the moment location transaction event ({@code ts}). */
    @SerializedName("ts")
    private String timestamp;
    /**
     * The original timestamp in a human readable timestamp format at the moment location transaction event ({@code
     * ts_friendly}).
     */
    @SerializedName("ts_friendly")
    private String friendlyTimestamp;
}
