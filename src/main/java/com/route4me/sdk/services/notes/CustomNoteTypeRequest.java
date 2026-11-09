package com.route4me.sdk.services.notes;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * A custom note type to add, the body of {@link NotesManager#addCustomNoteType(CustomNoteTypeRequest)}. The API reads
 * {@code type} and {@code values}, as the C# and PHP SDKs send them; it rejects the V4 spec's
 * {@code note_custom_type} and {@code note_custom_type_values} (400, "Field values is required").
 */
@Data
public class CustomNoteTypeRequest {
    /** The name of the custom note type (required) ({@code type}). */
    @SerializedName("type")
    private String noteCustomType;

    /** The values the type allows (required, at least one) ({@code values}). */
    @SerializedName("values")
    private List<String> noteCustomTypeValues;

    /**
     * The kind of field: {@code 1} select, {@code 2} text, {@code 3} numeric, {@code 4} integer; {@code null}, the
     * default, leaves it to the API ({@code note_custom_field_type}, from the V4 spec; the C# SDK does not send it).
     */
    @SerializedName("note_custom_field_type")
    private Integer noteCustomFieldType;
}
