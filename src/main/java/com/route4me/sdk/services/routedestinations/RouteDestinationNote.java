package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteDestinationNote {
    @SerializedName("note_id")
    private Integer noteId;

    @SerializedName("note_uuid")
    private String noteUuid;

    @SerializedName("upload_id")
    private String uploadId;

    @SerializedName("timestamp_added")
    private String timestampAdded;

    @SerializedName("activity_type")
    private String activityType;

    @SerializedName("upload_extension")
    private String uploadExtension;

    @SerializedName("upload_url")
    private String uploadUrl;

    @SerializedName("upload_type")
    private String uploadType;

    @SerializedName("contents")
    private String contents;

    @SerializedName("lat")
    private Float latitude;

    @SerializedName("lng")
    private Float longitude;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("custom_types")
    private List<RouteDestinationNoteCustomType> customTypes;
}
