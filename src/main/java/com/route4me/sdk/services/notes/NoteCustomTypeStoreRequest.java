package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The body of {@link NotesManagerV5#createNoteCustomType(NoteCustomTypeStoreRequest)}. */
@Data
public class NoteCustomTypeStoreRequest {
    /** Note custom type name (required) ({@code note_custom_type}). */
    @SerializedName("note_custom_type") private String noteCustomType;
    /** Note custom type values (required) ({@code note_custom_type_values}). */
    @SerializedName("note_custom_type_values") private List<String> noteCustomTypeValues;
}
