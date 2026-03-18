package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class NoteStoreBulkRequest {
    @SerializedName("notes") private List<NoteStoreRequest> notes;
}
