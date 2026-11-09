package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** A facility, as the API returns it. */
@Data
public class FacilityResource {
    /** Unique facility identifier (hex32 string) ({@code facility_id}). */
    @SerializedName("facility_id")
    private String facilityId;

    /** Root member ID ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Long rootMemberId;

    /** Facility alias/name ({@code facility_alias}). */
    @SerializedName("facility_alias")
    private String facilityAlias;

    /** Facility address ({@code address}). */
    @SerializedName("address")
    private String address;

    /** The coordinates of the facility ({@code coordinates}). */
    @SerializedName("coordinates")
    private FacilityCoordinates coordinates;

    /** Facility status: 1=ACTIVE, 2=INACTIVE, 3=IN_REVIEW ({@code status}). */
    @SerializedName("status")
    private Integer status;

    /** Facility types array ({@code facility_types}). */
    @SerializedName("facility_types")
    private List<FacilityTypeRelation> facilityTypes;

    /** Timestamp when facility was created ({@code created_at}). */
    @SerializedName("created_at")
    private Long createdAt;

    /** Timestamp when facility was last updated ({@code updated_at}). */
    @SerializedName("updated_at")
    private Long updatedAt;
}
