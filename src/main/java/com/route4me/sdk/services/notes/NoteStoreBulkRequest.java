package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The body of {@link NotesManagerV5#bulkCreateNotes(NoteStoreBulkRequest)}. */
@Data
public class NoteStoreBulkRequest {
    /** Array of notes to create (required); each needs {@code route_id} and {@code strNoteContents} ({@code notes}). */
    @SerializedName("notes") private List<NoteStoreRequest> notes;
    /** The device that adds the notes, such as {@code web} ({@code device_type}). */
    @SerializedName("device_type") private String deviceType;
}
