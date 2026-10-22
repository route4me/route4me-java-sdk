package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class NoteCustomTypeCollection {
    @SerializedName("data") private List<NoteCustomType> data;
}
