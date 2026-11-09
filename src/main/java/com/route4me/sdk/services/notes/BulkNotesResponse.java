package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
/** The answer of {@link NotesManagerV5#bulkCreateNotes(NoteStoreBulkRequest)}. */
@Data
public class BulkNotesResponse {
    /** Whether the notes were accepted ({@code status}). */
    @SerializedName("status") private Boolean status;
    /** Whether the notes are created later, by a background job ({@code async}). */
    @SerializedName("async") private Boolean async;
}
