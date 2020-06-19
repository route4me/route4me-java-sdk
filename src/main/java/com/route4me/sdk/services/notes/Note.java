package com.route4me.sdk.services.notes;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.routing.Constants;
import lombok.Data;

@Data
public class Note {
    @SerializedName("route_destination_id")
    private String routeDestinationId;
    @SerializedName("note_id")
    private Number noteId;
    @SerializedName("upload_extension")
    private String uploadExtension;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("upload_id")
    private String uploadId;
    @SerializedName("upload_type")
    private String uploadType;
    @SerializedName("device_type")
    private Constants.DeviceType deviceType;
    @SerializedName("lat")
    private Number lat;
    @SerializedName("lng")
    private Number lng;
    @SerializedName("upload_url")
    private String uploadURL;
    @SerializedName("ts_added")
    private Number tsAdded;
    @SerializedName("contents")
    private String content;
    @SerializedName("activity_type")
    private String activityType;
}
