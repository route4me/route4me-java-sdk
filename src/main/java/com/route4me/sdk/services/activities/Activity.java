package com.route4me.sdk.services.activities;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.users.User;
import lombok.Data;

/** An activity of the account: something that happened to a route, a destination, a note or a member. */
@Data
public class Activity {

    /** Activity ID ({@code activity_id}). */
    @SerializedName("activity_id")
    private String id;
    /** Activity Type. See dtd#L23 ({@code activity_type}). */
    @SerializedName("activity_type")
    private ActivityType type;
    /** Activity timestamp - The time when the activity occurred ({@code activity_timestamp}). */
    @SerializedName("activity_timestamp")
    private String timestamp;
    /** Activity message ({@code activity_message}). */
    @SerializedName("activity_message")
    private String message;
    /** Parent route ID ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;
    /** Parent route name ({@code route_name}). */
    @SerializedName("route_name")
    private String routeName;
    /**
     * The ID of the route destination. It is read from {@code route_destination_od}, not {@code route_destination_id}.
     */
    @SerializedName("route_destination_od")
    private String routeDestinationId;
    /** Note ID ({@code note_id}). */
    @SerializedName("note_id")
    private String noteId;
    /** Note type ({@code note_type}). */
    @SerializedName("note_type")
    private String noteType;
    /** Note contents ({@code note_contents}). */
    @SerializedName("note_contents")
    private String noteContents;
    /** URL to a note file ({@code note_file}). */
    @SerializedName("note_file")
    private String noteFile;
    /** Member information. */
    private User member;
}
