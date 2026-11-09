package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A facility type of a {@link FacilityResource}. */
@Data
public class FacilityTypeRelation {
    /** Facility type ID ({@code facility_type_id}). */
    @SerializedName("facility_type_id")
    private Integer facilityTypeId;

    /** Facility type alias/name ({@code facility_type_alias}). */
    @SerializedName("facility_type_alias")
    private String facilityTypeAlias;

    /** Is this the default facility type for this facility ({@code is_default}). */
    @SerializedName("is_default")
    private Boolean isDefault;
}
