package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The answer to {@link TrackingManager#setGPS(GPSPosition)}.
 *
 * <p>Matches {@code SetGpsResponse} of the C# SDK.
 */
@Data
public class SetGpsResponse {

    /** Whether the position was recorded. */
    @SerializedName("status")
    private Boolean status;

    /**
     * The ID of the group of points of this route transaction. The first position is sent
     * without one; send the returned value with the next positions of the same
     * transaction.
     */
    @SerializedName("tx_id")
    private String txId;
}
