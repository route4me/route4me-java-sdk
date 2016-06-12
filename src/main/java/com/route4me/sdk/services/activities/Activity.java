
package com.route4me.sdk.services.activities;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.users.User;
import lombok.Data;

@Data
public class Activity {

    @SerializedName("activity_id")
    private String activityId;
    @SerializedName("activity_type")
    private String activityType;
    @SerializedName("activity_timestamp")
    private String activityTimestamp;
    @SerializedName("activityMessage")
    private String activityMessage;
    @SerializedName("routeId")
    private String routeId;
    @SerializedName("routeName")
    private String routeName;
    @SerializedName("routeDestinationId")
    private String routeDestinationId;
    @SerializedName("noteId")
    private String noteId;
    @SerializedName("noteType")
    private String noteType;
    @SerializedName("noteContents")
    private String noteContents;
    @SerializedName("noteFile")
    private String noteFile;
    private User member;
}
