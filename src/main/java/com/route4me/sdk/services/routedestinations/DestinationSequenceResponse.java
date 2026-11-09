package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link RouteDestinationsManager#getDestinationSequence(DestinationSequenceRequest)}. */
@Data
public class DestinationSequenceResponse {
    /**
     * Array of input coordinate indices in the optimised visit order. The values are 0-based positions referencing the
     * original coordinates array ({@code optimized_sequence}).
     */
    @SerializedName("optimized_sequence")
    private int[] optimizedSequence;
}
