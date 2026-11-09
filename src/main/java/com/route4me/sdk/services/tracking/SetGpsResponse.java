package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The answer to {@link TrackingManager#setGPS(GPSPosition)}.
 *
 * <p>Matches {@code SetGpsResponse} of the C# SDK. When the server answers in another format than JSON,
 * {@code status} and {@code txId} are {@code null} and {@code answerText} holds the answer.
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

    /**
     * The answer as the server sent it, when it is not a JSON object (for example with {@code format} {@code csv},
     * {@code serialized} or {@code xml}); {@code null} when the answer was JSON. It is never sent or read as JSON.
     */
    private transient String answerText;
}
