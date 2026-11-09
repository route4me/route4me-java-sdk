package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The body of {@link FacilityManager#updateFacility(String, FacilityUpdateRequest)}. */
@Data
public class FacilityUpdateRequest {
    /** Facility alias/name ({@code facility_alias}). */
    @SerializedName("facility_alias")
    private String facilityAlias;

    /** Facility address ({@code address}). */
    @SerializedName("address")
    private String address;

    /** The coordinates of the facility ({@code coordinates}). */
    @SerializedName("coordinates")
    private FacilityCoordinates coordinates;

    /** Facility types array ({@code facility_types}). */
    @SerializedName("facility_types")
    private List<FacilityTypeAssignment> facilityTypes;

    /** Facility status: 1=ACTIVE, 2=INACTIVE, 3=IN_REVIEW ({@code status}). */
    @SerializedName("status")
    private Integer status;
}
