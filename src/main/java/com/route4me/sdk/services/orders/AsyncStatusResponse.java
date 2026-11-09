package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of the batch methods of {@link OrderManagerV5} that may run in the background. */
@Data
public class AsyncStatusResponse {
    /** Whether the request was accepted ({@code status}). */
    @SerializedName("status")
    private Boolean status;

    /** Whether a background job does the work ({@code async}). */
    @SerializedName("async")
    private Boolean async;
}
