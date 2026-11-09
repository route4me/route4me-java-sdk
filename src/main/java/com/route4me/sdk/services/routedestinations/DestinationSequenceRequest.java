package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The body of {@link RouteDestinationsManager#getDestinationSequence(DestinationSequenceRequest)}. */
@Data
public class DestinationSequenceRequest {
    /**
     * Array of coordinates to optimise the visit sequence for. Each entry must include a valid latitude and longitude
     * ({@code coordinates}).
     */
    @SerializedName("coordinates")
    private List<SequenceCoordinate> coordinates;
}
