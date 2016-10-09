package com.route4me.sdk.services.activities;

import com.google.gson.annotations.SerializedName;

public enum ActivityType {
    @SerializedName("delete-destination")
    DELETE_DESTINATION("delete-destination"),
    @SerializedName("insert-destination")
    INSERT_DESTINATION("insert-destination"),
    @SerializedName("mark-destination-departed")
    MARK_DESTINATION_DEPARTED("mark-destination-departed"),
    @SerializedName("mark-destination-visited")
    MARK_DESTINATION_VISITED("mark-destination-visited"),
    @SerializedName("member-created")
    MEMBER_CREATED("member-created"),
    @SerializedName("member-deleted")
    MEMBER_DELETED("member-deleted"),
    @SerializedName("member-modified")
    MEMBER_MODIFIED("member-modified"),
    @SerializedName("move-destination")
    MOVE_DESTINATION("move-destination"),
    @SerializedName("note-insert")
    NOTE_INSERT("note-insert"),
    @SerializedName("route-delete")
    ROUTE_DELETE("route-delete"),
    @SerializedName("route-optimized")
    ROUTE_OPTIMIZED("route-optimized"),
    @SerializedName("route-owner-changed")
    ROUTE_OWNER_CHANGED("route-owner-changed"),
    @SerializedName("update-destinations")
    UPDATE_DESTINATIONS("update-destinations"),
    @SerializedName("area-added")
    AREA_ADDED("area-added"),
    @SerializedName("area-removed")
    AREA_REMOVED("area-removed"),
    @SerializedName("area-updated")
    AREA_UPDATED("area-updated"),
    @SerializedName("destination-out-sequence")
    DESTINATION_OUT_SEQUENCE("destination-out-sequence"),
    @SerializedName("driver-arrived-early")
    DRIVER_ARRIVED_EARLY("driver-arrived-early"),
    @SerializedName("driver-arrived-on-time")
    DRIVER_ARRIVEDON_TIME("driver-arrived-on-time"),
    @SerializedName("driver-arrived-late")
    DRIVER_ARRIVED_LATE("driver-arrived-late"),
    @SerializedName("geofence-entered")
    GEOFENCE_ENTERED("geofence-entered"),
    @SerializedName("geofence-left")
    GEOFENCE_LEFT("geofence-left"),
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
