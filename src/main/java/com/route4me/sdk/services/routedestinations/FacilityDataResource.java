package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** A facility linked to a route destination. */
@Data
public class FacilityDataResource {
    /** Facility identifier (32-character hexadecimal UUID) ({@code facility_id}). */
    @SerializedName("facility_id")
    private Integer facilityId;

    /** Human-readable alias for the facility (e.g., "Main Warehouse") ({@code facility_alias}). */
    @SerializedName("facility_alias")
    private String facilityAlias;

    /** Status label for the facility (e.g., "Active") ({@code status}). */
    @SerializedName("status")
    private String status;

    /** The numeric status of the facility, such as {@code 1} for active ({@code status_code}). */
    @SerializedName("status_code")
    private String statusCode;

    /** Facility coordinates as a nested object with lat and lng ({@code coordinates}). */
    @SerializedName("coordinates")
    private String coordinates;

    /** Street address of the facility ({@code address}). */
    @SerializedName("address")
    private String address;

    /** List of facility types associated with this facility ({@code facility_types}). */
    @SerializedName("facility_types")
    private List<FacilityType> facilityTypes;
}
