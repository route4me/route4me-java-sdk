package com.route4me.sdk.services.notes;

import com.route4me.sdk.services.notes.Note;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Notes {

    private Boolean status;
    @SerializedName("note_id")
    private String noteId;
    @SerializedName("upload_id")
    private String uploadId;
    private Note note;
}
