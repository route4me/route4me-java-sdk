package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/**
 * The filters, sort order, page and fields of a location list, the body of the list methods of
 * {@link LocationManager}. The other location requests extend it.
 */
@Data
public class LocationCombinedRequest {
    /** Filter criteria for locations ({@code filters}). */
    @SerializedName("filters")
    private LocationFilters filters;

    /** Sort order as array of [field, direction] pairs (e.g., [["name", "asc"]]) ({@code order_by}). */
    @SerializedName("order_by")
    private List<String[]> orderBy;

    /** Page number for pagination ({@code page}). */
    @SerializedName("page")
    private Integer page;

    /** Number of items per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;

    /** Field name to group results by ({@code group_by}). */
    @SerializedName("group_by")
    private String groupBy;

    /** The fields to return for each location ({@code fields}). */
    @SerializedName("fields")
    private List<String> fields;

    /** Array of field names to include in the response ({@code selected_fields}). */
    @SerializedName("selected_fields")
    private List<String> selectedFields;
}
