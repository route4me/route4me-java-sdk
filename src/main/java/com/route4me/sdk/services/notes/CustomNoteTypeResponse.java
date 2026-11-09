package com.route4me.sdk.services.notes;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The answer of {@link NotesManager#addCustomNoteType(CustomNoteTypeRequest)} and
 * {@link NotesManager#removeCustomNoteType(long)}.
 */
@Data
public class CustomNoteTypeResponse {
    /** {@code OK} when the request succeeded ({@code result}). */
    @SerializedName("result")
    private String result;

    /** The number of custom note types added or deleted ({@code affected}). */
    @SerializedName("affected")
    private Integer affected;

    /** The added custom note type, when the API returns it ({@code data}). */
    @SerializedName("data")
    private CustomNoteType data;
}
