package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * The body of {@link LocationManager#getLocationsTerritories(LocationTerritoriesRequest)}: the location filters and
 * the page of territories.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LocationTerritoriesRequest extends LocationCombinedRequest {
    /** Page number for territories pagination (1-based) ({@code territories_page}). */
    @SerializedName("territories_page")
    private Integer territoriesPage;

    /** Number of territories per page ({@code territories_per_page}). */
    @SerializedName("territories_per_page")
    private Integer territoriesPerPage;

    /** Search query to filter territories by name ({@code territories_search_query}). */
    @SerializedName("territories_search_query")
    private String territoriesSearchQuery;

    /** Sort order for territories as array of [field, direction] pairs ({@code territories_order_by}). */
    @SerializedName("territories_order_by")
    private List<String[]> territoriesOrderBy;
}
