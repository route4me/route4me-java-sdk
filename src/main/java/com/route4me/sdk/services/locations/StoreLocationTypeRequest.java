package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class StoreLocationTypeRequest {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("is_parent_type")
    private Boolean isParentType;

    @SerializedName("is_child_type")
    private Boolean isChildType;
}
