package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A custom note type value of a {@link RouteDestinationNote}. */
@Data
public class RouteDestinationNoteCustomType {
    /** Unique identifier for the custom entry (32-character hex UUID) ({@code note_custom_entry_id}). */
    @SerializedName("note_custom_entry_id")
    private String noteCustomEntryId;

    /** Identifier for the custom type definition ({@code note_custom_type_id}). */
    @SerializedName("note_custom_type_id")
    private Integer noteCustomTypeId;

    /** Value stored for this custom type ({@code note_custom_value}). */
    @SerializedName("note_custom_value")
    private String noteCustomValue;

    /** Name/label of the custom type ({@code note_custom_type}). */
    @SerializedName("note_custom_type")
    private String noteCustomType;
}
