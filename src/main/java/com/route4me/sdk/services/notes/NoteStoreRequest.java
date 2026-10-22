package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
/** A note to add, the body of {@link NotesManagerV5#createNote(NoteStoreRequest)}. */
@Data
public class NoteStoreRequest {
    /** Route ID (required, 32-char hex string) ({@code route_id}). */
    @SerializedName("route_id") private String routeId;
    /** Address ID (nullable, integer) ({@code address_id}). */
    @SerializedName("address_id") private Integer addressId;
    /** Note contents (required) ({@code strNoteContents}). */
    @SerializedName("strNoteContents") private String noteContents;
    /** The device that adds the note, such as {@code web} or {@code iphone} ({@code device_type}). */
    @SerializedName("device_type") private String deviceType;
    /** Latitude ({@code lat}). */
    @SerializedName("lat") private Double lat;
    /** Longitude ({@code lng}). */
    @SerializedName("lng") private Double lng;
    /** Activity Type ({@code activity_type}). */
    @SerializedName("activity_type") private String activityType;
}
