package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A facility type to give a facility, in a {@link FacilityCreateRequest} or a {@link FacilityUpdateRequest}. */
@Data
public class FacilityTypeAssignment {
    /** Facility type ID ({@code facility_type_id}). */
    @SerializedName("facility_type_id")
    private Integer facilityTypeId;

    /** Is this the default facility type for this facility ({@code is_default}). */
    @SerializedName("is_default")
    private Boolean isDefault;
}
