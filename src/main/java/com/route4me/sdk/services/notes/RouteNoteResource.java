package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
/** A note of a route or destination, from the V5 API. */
@Data
public class RouteNoteResource {
    /** The id of the note in the system ({@code note_id}). */
    @SerializedName("note_id") private Integer noteId;
    /** Note UUID (32-char hex string) ({@code note_uuid}). */
    @SerializedName("note_uuid") private String noteUuid;
    /** Upload ID (32-char hex string) ({@code upload_id}). */
    @SerializedName("upload_id") private String uploadId;
    /** Route ID (32-char hex string) ({@code route_id}). */
    @SerializedName("route_id") private String routeId;
    /** Route destination ID ({@code route_destination_id}). */
    @SerializedName("route_destination_id") private Long routeDestinationId;
    /** Destination UUID (32-char hex string) ({@code destination_uuid}). */
    @SerializedName("destination_uuid") private String destinationUuid;
    /** Timestamp when note was added (formatted string) ({@code ts_added}). */
    @SerializedName("ts_added") private String tsAdded;
    /** Activity type ({@code activity_type}). */
    @SerializedName("activity_type") private String activityType;
    /** The extension of the attachment ('pdf', 'csv' etc) ({@code upload_extension}). */
    @SerializedName("upload_extension") private String uploadExtension;
    /** The direct CDN URL of the attachment uploaded with a note ({@code upload_url}). */
    @SerializedName("upload_url") private String uploadUrl;
    /** Upload type ({@code upload_type}). */
    @SerializedName("upload_type") private String uploadType;
    /** Note contents/text ({@code contents}). */
    @SerializedName("contents") private String contents;
    /** Latitude ({@code lat}). */
    @SerializedName("lat") private Double lat;
    /** Longitude ({@code lng}). */
    @SerializedName("lng") private Double lng;
    /** The device the note was added from, such as {@code web} or {@code iphone} ({@code device_type}). */
    @SerializedName("device_type") private String deviceType;
}
