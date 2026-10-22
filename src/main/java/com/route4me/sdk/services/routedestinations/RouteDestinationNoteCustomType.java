package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RouteDestinationNoteCustomType {
    @SerializedName("note_custom_entry_id")
    private String noteCustomEntryId;

    @SerializedName("note_custom_type_id")
    private Integer noteCustomTypeId;

    @SerializedName("note_custom_value")
    private String noteCustomValue;

    @SerializedName("note_custom_type")
    private String noteCustomType;
}
