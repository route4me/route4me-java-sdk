package com.route4me.sdk.services.vehicles;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Optional filters of {@link VehiclesManagerV5#getVehicles(VehicleListRequest)}, sent as query parameters. Every
 * field left {@code null} is left out of the request; lists go out as {@code name[]}, booleans as {@code 1} or
 * {@code 0}.
 */
@Data
@Accessors(chain = true)
public class VehicleListRequest {

    /**
     * The vehicles to list ({@code show}): {@code active} (the API default), {@code pending}, {@code deleted} or
     * {@code all}. This is how to get the vehicles by state.
     */
    @QueryParameter("show")
    private String show;
    /** Text to look for in the vehicles, at most 200 characters ({@code search_query}). */
    @QueryParameter("search_query")
    private String searchQuery;
    /** An additional query string ({@code query}). */
    @QueryParameter("query")
    private String query;
    /** {@code true} to get only the operational vehicles ({@code is_operational}). */
    @QueryParameter("is_operational")
    private Boolean isOperational;
    /** {@code true} to get only the vehicles that have a vehicle profile ({@code only_with_profile}). */
    @QueryParameter("only_with_profile")
    private Boolean onlyWithProfile;
    /** {@code true} to get the multi-unit fields too ({@code with_multi_unit_fields}). */
    @QueryParameter("with_multi_unit_fields")
    private Boolean withMultiUnitFields;
    /** Only the vehicles linked to these telematics connections ({@code telematics_connection_ids[]}). */
    @QueryParameter("telematics_connection_ids")
    private List<Long> telematicsConnectionIds;
    /** Only the vehicles of these facilities, as 32-character hex IDs ({@code facility_ids[]}). */
    @QueryParameter("facility_ids")
    private List<String> facilityIds;
}
