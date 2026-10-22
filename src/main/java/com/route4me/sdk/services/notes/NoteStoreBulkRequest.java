package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The body of {@link NotesManagerV5#bulkCreateNotes(NoteStoreBulkRequest)}. */
@Data
public class NoteStoreBulkRequest {
    /** Array of notes to create ({@code notes}). */
    @SerializedName("notes") private List<NoteStoreRequest> notes;
}
