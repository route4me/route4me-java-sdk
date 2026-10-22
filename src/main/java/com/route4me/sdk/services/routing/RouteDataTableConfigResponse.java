package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * The answer of {@link RoutingManagerV5#getRouteDataTableConfig()}: the fields the route data table can sort and
 * filter by, and how it pages.
 */
@Data
public class RouteDataTableConfigResponse {
    /** What the data table can do ({@code api_capabilities}). */
    @SerializedName("api_capabilities")
    private ApiCapabilities apiCapabilities;

    /** How the data table prefers to be used ({@code api_preferences}). */
    @SerializedName("api_preferences")
    private ApiPreferences apiPreferences;

    /** The capabilities of the route data table. */
    @Data
    public static class ApiCapabilities {
        /** The fields the routes can be sorted by ({@code sortable_fields}). */
        @SerializedName("sortable_fields")
        private List<String> sortableFields;

        /** The combinations of fields the routes can be sorted by together ({@code sortable_fields_combinations}). */
        @SerializedName("sortable_fields_combinations")
        private List<List<String>> sortableFieldsCombinations;

        /** Whether the routes can be sorted by several fields ({@code multi_sorting_enabled}). */
        @SerializedName("multi_sorting_enabled")
        private Boolean multiSortingEnabled;

        /** The fields the routes can be filtered by ({@code filterable_fields}). */
        @SerializedName("filterable_fields")
        private List<String> filterableFields;

        /** Whether the routes can be searched ({@code search}). */
        @SerializedName("search")
        private Boolean search;
    }

    /** The preferences of the route data table. */
    @Data
    public static class ApiPreferences {
        /** Whether the search must run on the server ({@code force_server_side_search}). */
        @SerializedName("force_server_side_search")
        private Boolean forceServerSideSearch;

        /** Whether the rows load in parts ({@code partial_load}). */
        @SerializedName("partial_load")
        private Boolean partialLoad;

        /** Whether the pages have only next and previous links ({@code simple_pagination}). */
        @SerializedName("simple_pagination")
        private Boolean simplePagination;
    }
}
