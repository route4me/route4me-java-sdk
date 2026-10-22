package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/**
 * A note to add, the body of {@link NotesManagerV5#createNote(NoteStoreRequest)} and an item of
 * {@link NotesManagerV5#bulkCreateNotes(NoteStoreBulkRequest)}. The JSON names are those of the V5 spec and of the
 * C# SDK.
 */
@Data
public class NoteStoreRequest {
    /** Route ID (required, 32-char hex string) ({@code route_id}). */
    @SerializedName("route_id") private String routeId;
    /** Address ID (nullable, integer) ({@code address_id}). */
    @SerializedName("address_id") private Integer addressId;
    /** The UUID of the destination, instead of its address ID ({@code destination_uuid}). */
    @SerializedName("destination_uuid") private String destinationUuid;
    /** Note contents (required, up to 1000 characters) ({@code strNoteContents}). */
    @SerializedName("strNoteContents") private String noteContents;
    /** The device that adds the note, such as {@code web} or {@code iphone} ({@code device_type}). */
    @SerializedName("device_type") private String deviceType;
    /** The latitude of the device ({@code dev_lat}; the reply calls it {@code lat}). */
    @SerializedName("dev_lat") private Double lat;
    /** The longitude of the device ({@code dev_lng}; the reply calls it {@code lng}). */
    @SerializedName("dev_lng") private Double lng;
    /**
     * The note type, such as {@code dropoff} or {@code pickup} ({@code strUpdateType}; the reply calls it
     * {@code activity_type}).
     */
    @SerializedName("strUpdateType") private String activityType;
    /** The speed of the device, in m/s ({@code remote_speed}). */
    @SerializedName("remote_speed") private Double remoteSpeed;
    /** The altitude of the device, in meters ({@code remote_altitude}). */
    @SerializedName("remote_altitude") private Double remoteAltitude;
    /** The values of the custom note types of the note ({@code custom_note_type}). */
    @SerializedName("custom_note_type") private List<String> customNoteType;
    /** When the note was taken, as a Unix time ({@code utc_time}). */
    @SerializedName("utc_time") private Long utcTime;
    /** The POD workflow the note belongs to, a 32-char hex ID ({@code workflow_id}). */
    @SerializedName("workflow_id") private String workflowId;
    /** The action of the POD workflow the note belongs to, a 32-char hex ID ({@code workflow_action_id}). */
    @SerializedName("workflow_action_id") private String workflowActionId;
    /** In a bulk create only: the ID of an attachment uploaded before ({@code upload_id}). */
    @SerializedName("upload_id") private String uploadId;
}
