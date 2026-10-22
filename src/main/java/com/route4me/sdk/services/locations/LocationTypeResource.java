package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A location type, as the API returns it. */
@Data
public class LocationTypeResource {
    /** Unique identifier for the location type ({@code location_type_id}). */
    @SerializedName("location_type_id")
    private String locationTypeId;

    /** Name of the location type ({@code name}). */
    @SerializedName("name")
    private String name;

    /** Description of the location type ({@code description}). */
    @SerializedName("description")
    private String description;

    /** Indicates if this type can have child locations ({@code is_parent_type}). */
    @SerializedName("is_parent_type")
    private Boolean isParentType;

    /** Indicates if this type can be a child of another location ({@code is_child_type}). */
    @SerializedName("is_child_type")
    private Boolean isChildType;

    /** Root member ID that owns this location type ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Integer rootMemberId;
}
