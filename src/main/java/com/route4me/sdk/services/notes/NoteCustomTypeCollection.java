package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** The custom note types, as the API returns them. */
@Data
public class NoteCustomTypeCollection {
    /** Array of custom type resources ({@code data}). */
    @SerializedName("data") private List<NoteCustomType> data;
}
