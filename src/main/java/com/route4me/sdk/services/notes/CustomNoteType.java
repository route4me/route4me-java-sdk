package com.route4me.sdk.services.notes;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** A custom note type of the V4 API, as {@link NotesManager#getAllCustomNoteTypes()} returns it. */
@Data
public class CustomNoteType {
    /** The custom note type ID ({@code note_custom_type_id}). */
    @SerializedName("note_custom_type_id")
    private Long noteCustomTypeId;

    /** The name of the custom note type ({@code note_custom_type}). */
    @SerializedName("note_custom_type")
    private String noteCustomType;

    /** The member ID of the account owner ({@code root_owner_member_id}). */
    @SerializedName("root_owner_member_id")
    private Long rootOwnerMemberId;

    /**
     * The kind of field: {@code 1} select, {@code 2} text, {@code 3} numeric, {@code 4} integer
     * ({@code note_custom_field_type}).
     */
    @SerializedName("note_custom_field_type")
    private Integer noteCustomFieldType;

    /** The values the type allows ({@code note_custom_type_values}). */
    @SerializedName("note_custom_type_values")
    private List<String> noteCustomTypeValues;
}
