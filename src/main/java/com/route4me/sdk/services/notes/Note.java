package com.route4me.sdk.services.notes;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.services.routing.Constants;
import lombok.Data;

/** A note of a route destination, from the V4 API. */
@Data
public class Note {
    /** The route destination ID ({@code route_destination_id}). */
    @SerializedName("route_destination_id")
    private String routeDestinationId;
    /** An unique ID of a note ({@code note_id}). */
    @SerializedName("note_id")
    private Number noteId;
    /** An extension of the uploaded file ({@code upload_extension}). */
    @SerializedName("upload_extension")
    private String uploadExtension;
    /** The route ID ({@code route_id}). */
    @SerializedName("route_id")
    private String routeId;
    /** An unique ID of an uploaded file ({@code upload_id}). */
    @SerializedName("upload_id")
    private String uploadId;
    /** An upload type of the note ({@code upload_type}). */
    @SerializedName("upload_type")
    private String uploadType;
    /** The device a note was uploaded from ({@code device_type}). */
    @SerializedName("device_type")
    private Constants.DeviceType deviceType;
    /** The position latitude where the address note was added ({@code lat}). */
    @SerializedName("lat")
    private Number lat;
    /** The position longitude where the address note was added ({@code lng}). */
    @SerializedName("lng")
    private Number lng;
    /** An upload url - where a file-note was uploaded ({@code upload_url}). */
    @SerializedName("upload_url")
    private String uploadURL;
    /** When the note was added ({@code ts_added}). */
    @SerializedName("ts_added")
    private Number tsAdded;
    /** The note text contents ({@code contents}). */
    @SerializedName("contents")
    private String content;
    /** The activity type of the note, such as {@code note-insert} ({@code activity_type}). */
    @SerializedName("activity_type")
    private String activityType;
}
