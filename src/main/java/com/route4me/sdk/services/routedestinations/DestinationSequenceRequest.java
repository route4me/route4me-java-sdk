package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class DestinationSequenceRequest {
    @SerializedName("coordinates")
    private List<SequenceCoordinate> coordinates;
}
