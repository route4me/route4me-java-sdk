package com.route4me.sdk.services.users;

import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * The filters of {@link TeamManagementManagerV5#getTeamMembers(TeamUsersQuery)}; fields left {@code null} are not
 * sent. Lists are sent with brackets ({@code facility_ids[]=...}, {@code order_by[0][]=...}), as the V5 API reads
 * them, and {@code drivers_only} as {@code 1} or {@code 0}.
 */
@Data
@Accessors(chain = true)
public class TeamUsersQuery {
    /** Text to search for in the members, such as an email address ({@code query}). */
    @QueryParameter("query")
    private String query;
    /** {@code true} to list only drivers ({@code drivers_only}). */
    @QueryParameter("drivers_only")
    private Boolean driversOnly;
    /** Only the members of this optimization ({@code optimization_problem_id}). */
    @QueryParameter("optimization_problem_id")
    private String optimizationProblemId;
    /** The sort order: pairs of a field and {@code asc} or {@code desc} ({@code order_by}). */
    @QueryParameter("order_by")
    private List<List<String>> orderBy;
    /** Only the members of these facilities, by facility ID ({@code facility_ids}). */
    @QueryParameter("facility_ids")
    private List<String> facilityIds;
}
