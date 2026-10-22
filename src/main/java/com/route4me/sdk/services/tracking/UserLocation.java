package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import lombok.Data;

/** A user and their last known position, from {@link TrackingManager#getUserLocations(String)}. */
@Data
public class UserLocation {
    /** The user: member ID, name, e-mail and type, by their API names ({@code member_data}). */
    @SerializedName("member_data")
    private Map<String, Object> memberData;
    /** The last known position ({@code tracking}). */
    @SerializedName("tracking")
    private UserTracking tracking;
    /** Whether the position came from the cache ({@code from_cache}). */
    @SerializedName("from_cache")
    private Boolean fromCache;
}
