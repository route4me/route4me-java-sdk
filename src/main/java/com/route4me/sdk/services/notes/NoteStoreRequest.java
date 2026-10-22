package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
@Data
public class NoteStoreRequest {
    @SerializedName("route_id") private String routeId;
    @SerializedName("address_id") private Integer addressId;
    @SerializedName("strNoteContents") private String noteContents;
    @SerializedName("device_type") private String deviceType;
    @SerializedName("lat") private Double lat;
    @SerializedName("lng") private Double lng;
    @SerializedName("activity_type") private String activityType;
}
