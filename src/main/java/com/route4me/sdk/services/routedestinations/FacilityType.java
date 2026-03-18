package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FacilityType {
    @SerializedName("facility_type_id")
    private Integer facilityTypeId;

    @SerializedName("facility_type_alias")
    private String facilityTypeAlias;

    @SerializedName("is_default")
    private Boolean isDefault;
}
