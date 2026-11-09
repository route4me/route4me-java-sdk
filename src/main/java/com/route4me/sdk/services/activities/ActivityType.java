package com.route4me.sdk.services.activities;

import com.google.gson.annotations.SerializedName;

/** The types of activity; {@code toString()} gives the API value ({@code activity_type}). */
public enum ActivityType {
    /** A destination was removed from a route ({@code delete-destination}). */
    @SerializedName("delete-destination")
    DELETE_DESTINATION("delete-destination"),
    /** A destination was added to a route ({@code insert-destination}). */
    @SerializedName("insert-destination")
    INSERT_DESTINATION("insert-destination"),
    /** A destination was marked as departed ({@code mark-destination-departed}). */
    @SerializedName("mark-destination-departed")
    MARK_DESTINATION_DEPARTED("mark-destination-departed"),
    /** A destination was marked as visited ({@code mark-destination-visited}). */
    @SerializedName("mark-destination-visited")
    MARK_DESTINATION_VISITED("mark-destination-visited"),
    /** A member was created ({@code member-created}). */
    @SerializedName("member-created")
    MEMBER_CREATED("member-created"),
    /** A member was deleted ({@code member-deleted}). */
    @SerializedName("member-deleted")
    MEMBER_DELETED("member-deleted"),
    /** A member was changed ({@code member-modified}). */
    @SerializedName("member-modified")
    MEMBER_MODIFIED("member-modified"),
    /** A destination was moved to another route ({@code move-destination}). */
    @SerializedName("move-destination")
    MOVE_DESTINATION("move-destination"),
    /** A note was added ({@code note-insert}). */
    @SerializedName("note-insert")
    NOTE_INSERT("note-insert"),
    /** A route was deleted ({@code route-delete}). */
    @SerializedName("route-delete")
    ROUTE_DELETE("route-delete"),
    /** A route was optimized ({@code route-optimized}). */
    @SerializedName("route-optimized")
    ROUTE_OPTIMIZED("route-optimized"),
    /** The owner of a route changed ({@code route-owner-changed}). */
    @SerializedName("route-owner-changed")
    ROUTE_OWNER_CHANGED("route-owner-changed"),
    /** Destinations were changed ({@code update-destinations}). */
    @SerializedName("update-destinations")
    UPDATE_DESTINATIONS("update-destinations"),
    /** An area (territory) was added ({@code area-added}). */
    @SerializedName("area-added")
    AREA_ADDED("area-added"),
    /** An area (territory) was removed ({@code area-removed}). */
    @SerializedName("area-removed")
    AREA_REMOVED("area-removed"),
    /** An area (territory) was changed ({@code area-updated}). */
    @SerializedName("area-updated")
    AREA_UPDATED("area-updated"),
    /** A destination was visited out of sequence ({@code destination-out-sequence}). */
    @SerializedName("destination-out-sequence")
    DESTINATION_OUT_SEQUENCE("destination-out-sequence"),
    /** A driver arrived early ({@code driver-arrived-early}). */
    @SerializedName("driver-arrived-early")
    DRIVER_ARRIVED_EARLY("driver-arrived-early"),
    /** A driver arrived on time ({@code driver-arrived-on-time}). */
    @SerializedName("driver-arrived-on-time")
    DRIVER_ARRIVED_ON_TIME("driver-arrived-on-time"),
    /** A driver arrived late ({@code driver-arrived-late}). */
    @SerializedName("driver-arrived-late")
    DRIVER_ARRIVED_LATE("driver-arrived-late"),
    /** A driver entered the geofence of a destination ({@code geofence-entered}). */
    @SerializedName("geofence-entered")
    GEOFENCE_ENTERED("geofence-entered"),
    /** A driver left the geofence of a destination ({@code geofence-left}). */
    @SerializedName("geofence-left")
    GEOFENCE_LEFT("geofence-left"),
    /** A user posted a message ({@code user_message}). */
    @SerializedName("user_message")
    USER_MESSAGE("user_message");

    private final String raw;

    ActivityType(String raw) {
        this.raw = raw;
    }

    @Override
    public String toString() {
        return this.raw;
    }
}
