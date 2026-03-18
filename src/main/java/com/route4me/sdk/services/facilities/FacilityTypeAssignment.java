package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FacilityTypeAssignment {
    @SerializedName("facility_type_id")
    private Integer facilityTypeId;

    @SerializedName("is_default")
    private Boolean isDefault;
}
