package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LocationTypeResource {
    @SerializedName("location_type_id")
    private String locationTypeId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("is_parent_type")
    private Boolean isParentType;

    @SerializedName("is_child_type")
    private Boolean isChildType;

    @SerializedName("root_member_id")
    private Integer rootMemberId;
}
