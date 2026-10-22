package com.route4me.sdk.services.notes;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * A custom note type to add, the body of {@link NotesManager#addCustomNoteType(CustomNoteTypeRequest)}. The JSON
 * names are those of the V4 spec ({@code Route4Me-note-custom-types-api-4-oas3.yaml}).
 */
@Data
public class CustomNoteTypeRequest {
    /** The name of the custom note type (required) ({@code note_custom_type}). */
    @SerializedName("note_custom_type")
    private String noteCustomType;

    /** The values the type allows ({@code note_custom_type_values}). */
    @SerializedName("note_custom_type_values")
    private List<String> noteCustomTypeValues;

    /**
     * The kind of field: {@code 1} select, {@code 2} text, {@code 3} numeric, {@code 4} integer; {@code null} leaves
     * it to the API ({@code note_custom_field_type}).
     */
    @SerializedName("note_custom_field_type")
    private Integer noteCustomFieldType;
}
