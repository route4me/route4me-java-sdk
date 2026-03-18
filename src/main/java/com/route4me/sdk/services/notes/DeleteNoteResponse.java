package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
@Data
public class DeleteNoteResponse {
    @SerializedName("status") private Boolean status;
    @SerializedName("note_id") private Integer noteId;
}
