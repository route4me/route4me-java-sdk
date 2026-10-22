package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The paging data ({@code meta}) of a paginated V5 vehicle list. */
@Data
public class VehiclePageMeta {
    /** The page number ({@code current_page}). */
    @SerializedName("current_page")
    private Integer currentPage;
    /** The position of the first item of the page, from 1 ({@code from}). */
    @SerializedName("from")
    private Integer from;
    /** The number of the last page ({@code last_page}). */
    @SerializedName("last_page")
    private Integer lastPage;
    /** The URL of the list, without the paging parameters ({@code path}). */
    @SerializedName("path")
    private String path;
    /** The number of items per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;
    /** The position of the last item of the page ({@code to}). */
    @SerializedName("to")
    private Integer to;
    /** The number of items on all pages ({@code total}). */
    @SerializedName("total")
    private Integer total;
}
