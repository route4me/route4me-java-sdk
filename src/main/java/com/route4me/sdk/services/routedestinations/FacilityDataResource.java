package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class FacilityDataResource {
    @SerializedName("facility_id")
    private Integer facilityId;

    @SerializedName("facility_alias")
    private String facilityAlias;

    @SerializedName("status")
    private String status;

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("coordinates")
    private String coordinates;

    @SerializedName("address")
    private String address;

    @SerializedName("facility_types")
    private List<FacilityType> facilityTypes;
}
