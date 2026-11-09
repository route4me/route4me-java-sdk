package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A facility type of a {@link FacilityDataResource}. */
@Data
public class FacilityType {
    /** Unique identifier for the facility type ({@code facility_type_id}). */
    @SerializedName("facility_type_id")
    private Integer facilityTypeId;

    /** Human-readable alias for the facility type (e.g., "Cold Storage") ({@code facility_type_alias}). */
    @SerializedName("facility_type_alias")
    private String facilityTypeAlias;

    /** Whether this is the default facility type ({@code is_default}). */
    @SerializedName("is_default")
    private Boolean isDefault;
}
