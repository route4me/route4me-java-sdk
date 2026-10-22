package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The body of {@link RouteDestinationsManager#getDestinationsList(GetDestinationsRequest)}. */
@Data
public class GetDestinationsRequest {
    /** Destination-level filter criteria. At minimum the filters property must be present ({@code filters}). */
    @SerializedName("filters")
    private DestinationFilters filters;

    /**
     * Sort criteria as an array of [fieldName, direction] pairs. Direction must be "asc" or "desc". Example:
     * [["arrival_time", "desc"], ["destination_alias", "asc"]] ({@code order_by}).
     */
    @SerializedName("order_by")
    private List<String[]> orderBy;

    /** Page number (1-based). Defaults to 1 ({@code page}). */
    @SerializedName("page")
    private Integer page;

    /** Number of items per page (1–500) ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;

    /** Field name to group results by ({@code group_by}). */
    @SerializedName("group_by")
    private String groupBy;

    /** IANA timezone identifier used for date-time display (e.g., "America/New_York") ({@code timezone}). */
    @SerializedName("timezone")
    private String timezone;

    /**
     * Subset of destination fields to include in the response. Omit to return all available fields ({@code fields}).
     */
    @SerializedName("fields")
    private List<String> fields;
}
