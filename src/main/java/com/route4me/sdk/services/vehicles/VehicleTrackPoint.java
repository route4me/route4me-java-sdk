package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** One position of a vehicle track, from {@link VehiclesManagerV5#getVehicleTrack(String, String, String)}. */
@Data
public class VehicleTrackPoint {
    /** The route the vehicle was on, a 32-character hex ID ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;
    /** The vehicle ID, which the C# SDK reads too ({@code vehicle_id}). */
    @SerializedName("vehicle_id")
    private String vehicleId;
    /** The member who drove the vehicle ({@code member_id}). */
    @SerializedName("member_id")
    private Long memberId;
    /** The latitude ({@code lat}). */
    @SerializedName("lat")
    private Double lat;
    /** The longitude ({@code lng}). */
    @SerializedName("lng")
    private Double lng;
    /** The altitude ({@code altitude}). */
    @SerializedName("altitude")
    private Double altitude;
    /** The speed ({@code speed}). */
    @SerializedName("speed")
    private Double speed;
    /** When the position was taken, as a Unix timestamp in seconds ({@code timestamp}). */
    @SerializedName("timestamp")
    private Long timestamp;
}
