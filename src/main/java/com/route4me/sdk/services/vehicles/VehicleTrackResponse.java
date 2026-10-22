package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link VehiclesManagerV5#getVehicleTrack(String, String, String)}: the track of a vehicle. */
@Data
public class VehicleTrackResponse {
    /** The positions of the track ({@code data}). */
    @SerializedName("data")
    private List<VehicleTrackPoint> data;
}
