package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of the batch methods of {@link OrderManagerV5} that may run in the background. */
@Data
public class AsyncStatusResponse {
    /** If true, an optimization removed from database ({@code status}). */
    @SerializedName("status")
    private Boolean status;

    /** If true, a background job removes the optimizations ({@code async}). */
    @SerializedName("async")
    private Boolean async;
}
