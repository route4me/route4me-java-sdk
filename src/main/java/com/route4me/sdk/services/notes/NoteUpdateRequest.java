package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
@Data
public class NoteUpdateRequest {
    @SerializedName("strNoteContents") private String noteContents;
    @SerializedName("upload_url") private String uploadUrl;
}
