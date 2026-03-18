package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class FacilityCreateRequest {
    @SerializedName("facility_alias")
    private String facilityAlias;

    @SerializedName("address")
    private String address;

    @SerializedName("coordinates")
    private FacilityCoordinates coordinates;

    @SerializedName("facility_types")
    private List<FacilityTypeAssignment> facilityTypes;

    @SerializedName("status")
    private Integer status;
}
