package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
/** The body of {@link NotesManagerV5#updateNote(String, NoteUpdateRequest)}. */
@Data
public class NoteUpdateRequest {
    /** Note contents ({@code strNoteContents}). */
    @SerializedName("strNoteContents") private String noteContents;
    /** The URL of a file to attach to the note ({@code upload_url}). */
    @SerializedName("upload_url") private String uploadUrl;
}
