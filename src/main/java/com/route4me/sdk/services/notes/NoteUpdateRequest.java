package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The body of {@link NotesManagerV5#updateNote(String, NoteUpdateRequest)}. */
@Data
public class NoteUpdateRequest {
    /** Note contents ({@code strNoteContents}). */
    @SerializedName("strNoteContents") private String noteContents;
    /** The note type, such as {@code dropoff} ({@code strUpdateType}). */
    @SerializedName("strUpdateType") private String updateType;
    /** The device that changes the note, such as {@code web} ({@code device_type}). */
    @SerializedName("device_type") private String deviceType;
    /** The values of the custom note types of the note ({@code custom_note_type}). */
    @SerializedName("custom_note_type") private List<String> customNoteType;
    /** The POD workflow the note belongs to, a 32-char hex ID ({@code workflow_id}). */
    @SerializedName("workflow_id") private String workflowId;
    /** The action of the POD workflow the note belongs to, a 32-char hex ID ({@code workflow_action_id}). */
    @SerializedName("workflow_action_id") private String workflowActionId;
    /** The URL of a file to attach to the note; the V5 spec does not list it ({@code upload_url}). */
    @SerializedName("upload_url") private String uploadUrl;
}
