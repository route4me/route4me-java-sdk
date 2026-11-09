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
    /**
     * The kind of field: {@code 1} select, {@code 2} text, {@code 3} numeric, {@code 4} integer
     * ({@code note_custom_field_type}).
     */
    @SerializedName("note_custom_field_type") private Integer noteCustomFieldType;
}
