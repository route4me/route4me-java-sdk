package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * The body of {@link LocationManager#createLocationType(StoreLocationTypeRequest)} and
 * {@link LocationManager#updateLocationType(String, StoreLocationTypeRequest)}.
 */
@Data
public class StoreLocationTypeRequest {
    /** Name of the location type (required) ({@code name}). */
    @SerializedName("name")
    private String name;

    /** Description of the location type ({@code description}). */
    @SerializedName("description")
    private String description;

    /** Whether this type can have child locations ({@code is_parent_type}). */
    @SerializedName("is_parent_type")
    private Boolean isParentType;

    /** Whether this type can be a child of another location ({@code is_child_type}). */
    @SerializedName("is_child_type")
    private Boolean isChildType;
}
