package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/**
 * A page of facilities, the answer of
 * {@link FacilityManager#getFacilities(Integer, Integer, Integer, Integer, String)}.
 */
@Data
public class FacilitiesPaginateResource {
    /** The facilities of this page ({@code data}). */
    @SerializedName("data")
    private List<FacilityResource> data;

    /** Current page number ({@code current_page}). */
    @SerializedName("current_page")
    private Integer currentPage;

    /** The number of facilities in all the pages ({@code total}). */
    @SerializedName("total")
    private Integer total;

    /** The number of facilities per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;

    /** Last page number ({@code last_page}). */
    @SerializedName("last_page")
    private Integer lastPage;

    /** URL to the next page ({@code next_page_url}). */
    @SerializedName("next_page_url")
    private String nextPageUrl;

    /** URL to the previous page ({@code prev_page_url}). */
    @SerializedName("prev_page_url")
    private String prevPageUrl;

    /** Starting item number on current page ({@code from}). */
    @SerializedName("from")
    private Integer from;

    /** Ending item number on current page ({@code to}). */
    @SerializedName("to")
    private Integer to;
}
