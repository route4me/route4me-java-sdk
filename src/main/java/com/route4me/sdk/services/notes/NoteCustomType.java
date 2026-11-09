package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** A custom note type, with the values it allows. */
@Data
public class NoteCustomType {
    /** The custom note type ID ({@code note_custom_type_id}). */
    @SerializedName("note_custom_type_id") private Integer noteCustomTypeId;
    /** The custom type ({@code note_custom_type}). */
    @SerializedName("note_custom_type") private String noteCustomType;
    /** An array of the custom type note values ({@code note_custom_type_values}). */
    @SerializedName("note_custom_type_values") private List<String> noteCustomTypeValues;
}
