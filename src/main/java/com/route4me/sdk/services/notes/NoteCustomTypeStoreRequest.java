package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class NoteCustomTypeStoreRequest {
    @SerializedName("note_custom_type") private String noteCustomType;
    @SerializedName("note_custom_type_values") private List<String> noteCustomTypeValues;
}
