package com.route4me.sdk.services.activities;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.users.User;
import lombok.Data;

@Data
public class Activity {

    @SerializedName("activity_id")
    private String Id;
    @SerializedName("activity_type")
    private ActivityType type;
    @SerializedName("activity_timestamp")
    private String timestamp;
    @SerializedName("activity_message")
    private String message;
    @SerializedName("route_id")
    private String routeId;
    @SerializedName("route_name")
    private String routeName;
    @SerializedName("route_destination_od")
    private String routeDestinationId;
    @SerializedName("note_id")
    private String noteId;
    @SerializedName("note_type")
    private String noteType;
    @SerializedName("note_contents")
    private String noteContents;
    @SerializedName("note_file")
    private String noteFile;
    private User member;
}
