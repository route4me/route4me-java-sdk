package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FacilityTypeRelation {
    @SerializedName("facility_type_id")
    private Integer facilityTypeId;

    @SerializedName("facility_type_alias")
    private String facilityTypeAlias;

    @SerializedName("is_default")
    private Boolean isDefault;
}
