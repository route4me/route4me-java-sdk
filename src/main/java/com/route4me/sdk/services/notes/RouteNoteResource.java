package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
@Data
public class RouteNoteResource {
    @SerializedName("note_id") private Integer noteId;
    @SerializedName("note_uuid") private String noteUuid;
    @SerializedName("upload_id") private String uploadId;
    @SerializedName("route_id") private String routeId;
    @SerializedName("route_destination_id") private Integer routeDestinationId;
    @SerializedName("destination_uuid") private String destinationUuid;
    @SerializedName("ts_added") private String tsAdded;
    @SerializedName("activity_type") private String activityType;
    @SerializedName("upload_extension") private String uploadExtension;
    @SerializedName("upload_url") private String uploadUrl;
    @SerializedName("upload_type") private String uploadType;
    @SerializedName("contents") private String contents;
    @SerializedName("lat") private Double lat;
    @SerializedName("lng") private Double lng;
    @SerializedName("device_type") private String deviceType;
}
