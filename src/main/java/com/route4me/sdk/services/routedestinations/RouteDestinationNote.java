package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** A note of a route destination. */
@Data
public class RouteDestinationNote {
    /** Unique integer identifier for the note ({@code note_id}). */
    @SerializedName("note_id")
    private Integer noteId;

    /** UUID for the note (32-character hex string) ({@code note_uuid}). */
    @SerializedName("note_uuid")
    private String noteUuid;

    /** UUID of the associated file upload (32-character hex string) ({@code upload_id}). */
    @SerializedName("upload_id")
    private String uploadId;

    /** ISO 8601 timestamp when the note was added ({@code timestamp_added}). */
    @SerializedName("timestamp_added")
    private String timestampAdded;

    /** Activity type label (e.g., "note", "photo") ({@code activity_type}). */
    @SerializedName("activity_type")
    private String activityType;

    /** File extension of the uploaded asset (e.g., "jpg", "pdf") ({@code upload_extension}). */
    @SerializedName("upload_extension")
    private String uploadExtension;

    /** Public URL to access the uploaded file ({@code upload_url}). */
    @SerializedName("upload_url")
    private String uploadUrl;

    /** MIME type category of the upload (e.g., "image", "document") ({@code upload_type}). */
    @SerializedName("upload_type")
    private String uploadType;

    /** Text content of the note ({@code contents}). */
    @SerializedName("contents")
    private String contents;

    /** Latitude where the note was recorded ({@code lat}). */
    @SerializedName("lat")
    private Float latitude;

    /** Longitude where the note was recorded ({@code lng}). */
    @SerializedName("lng")
    private Float longitude;

    /** Type of device used to add the note (e.g., "mobile", "web") ({@code device_type}). */
    @SerializedName("device_type")
    private String deviceType;

    /** Additional structured custom type entries associated with this note ({@code custom_types}). */
    @SerializedName("custom_types")
    private List<RouteDestinationNoteCustomType> customTypes;
}
