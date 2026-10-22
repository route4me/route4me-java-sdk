package com.route4me.sdk.services.vehicles;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Optional paging, sorting and filters of {@link VehiclesManagerV5#getPaginatedVehiclesList(VehiclePaginateRequest)},
 * sent as query parameters. Every field left {@code null} is left out of the request; lists go out as
 * {@code name[]}, booleans as {@code 1} or {@code 0}.
 */
@Data
@Accessors(chain = true)
public class VehiclePaginateRequest {

    /** The page number, from 1 ({@code page}). */
    @QueryParameter("page")
    private Integer page;
    /** The number of vehicles per page ({@code per_page}). */
    @QueryParameter("per_page")
    private Integer perPage;
    /**
     * The sort order, as {@code [field, direction]} pairs such as {@code [["vehicle_alias", "asc"]]}, sent as
     * {@code order_by[0][]=vehicle_alias&order_by[0][]=asc} ({@code order_by}).
     */
    @QueryParameter("order_by")
    private List<List<String>> orderBy;
    /**
     * The vehicles to list ({@code show}): {@code active} (the API default), {@code pending}, {@code deleted} or
     * {@code all}.
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
    /**
     * Only these fields of each vehicle ({@code fields[]}); {@code vehicle_id} always comes back. {@code GET
     * https://wh.route4me.com/modules/api/v5.0/vehicles/fields} lists the allowed names.
     */
    @QueryParameter("fields")
    private List<String> fields;
}
