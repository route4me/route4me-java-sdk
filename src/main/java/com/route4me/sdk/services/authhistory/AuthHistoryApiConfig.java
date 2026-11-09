package com.route4me.sdk.services.authhistory;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The configuration of the authentication history API, as {@link AuthHistoryManager#getApiConfig()} returns it. */
@Data
public class AuthHistoryApiConfig {
    /** What the list of log-ins can do ({@code api_capabilities}). */
    @SerializedName("api_capabilities")
    private Capabilities apiCapabilities;

    /** How the web app should load the list ({@code api_preferences}). */
    @SerializedName("api_preferences")
    private Preferences apiPreferences;

    /** The fields the list of log-ins can sort and filter by, and whether it can search. */
    @Data
    public static class Capabilities {
        /** The fields the list can be sorted by ({@code sortable_fields}). */
        @SerializedName("sortable_fields")
        private List<String> sortableFields;

        /** The fields the list can be filtered by ({@code filterable_fields}). */
        @SerializedName("filterable_fields")
        private List<String> filterableFields;

        /** Whether the list can be searched ({@code search}). */
        @SerializedName("search")
        private Boolean search;
    }

    /** How the web app should load the list of log-ins. */
    @Data
    public static class Preferences {
        /** Whether searches must run on the server ({@code force_server_side_search}). */
        @SerializedName("force_server_side_search")
        private Boolean forceServerSideSearch;

        /** Whether the list loads one part at a time ({@code partial_load}). */
        @SerializedName("partial_load")
        private Boolean partialLoad;

        /** Whether the list uses simple pagination ({@code simple_pagination}). */
        @SerializedName("simple_pagination")
        private Boolean simplePagination;
    }
}
