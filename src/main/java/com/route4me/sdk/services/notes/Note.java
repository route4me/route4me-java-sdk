package com.route4me.sdk.services.notes;

import com.google.gson.annotations.SerializedName;
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
    private String deviceType;
    private Number lat;
    private Number lng;
    @SerializedName("upload_url")
    private String uploadUrl;
    @SerializedName("ts_added")
    private Number tsAdded;
    private String contents;
    @SerializedName("activity_type")
    private String activityType;
}
