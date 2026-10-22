package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DestinationSequenceResponse {
    @SerializedName("optimized_sequence")
    private int[] optimizedSequence;
}
