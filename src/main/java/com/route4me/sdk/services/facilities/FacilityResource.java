package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class FacilityResource {
    @SerializedName("facility_id")
    private String facilityId;

    @SerializedName("root_member_id")
    private Long rootMemberId;

    @SerializedName("facility_alias")
    private String facilityAlias;

    @SerializedName("address")
    private String address;

    @SerializedName("coordinates")
    private FacilityCoordinates coordinates;

    @SerializedName("status")
    private Integer status;

    @SerializedName("facility_types")
    private List<FacilityTypeRelation> facilityTypes;

    @SerializedName("created_at")
    private Long createdAt;

    @SerializedName("updated_at")
    private Long updatedAt;
}
