package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
/** The answer of {@link NotesManagerV5#deleteNote(String)}. */
@Data
public class DeleteNoteResponse {
    /** Status of deletion ({@code status}). */
    @SerializedName("status") private Boolean status;
    /** Deleted note ID ({@code note_id}). */
    @SerializedName("note_id") private Integer noteId;
}
