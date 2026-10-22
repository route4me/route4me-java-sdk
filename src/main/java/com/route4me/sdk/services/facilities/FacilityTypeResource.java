package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A facility type, as the API returns it. */
@Data
public class FacilityTypeResource {
    /** Facility type ID ({@code facility_type_id}). */
    @SerializedName("facility_type_id")
    private Integer facilityTypeId;

    /** Facility type alias/name ({@code facility_type_alias}). */
    @SerializedName("facility_type_alias")
    private String facilityTypeAlias;
}
