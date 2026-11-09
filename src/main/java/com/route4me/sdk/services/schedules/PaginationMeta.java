package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The position of a page in a paged list. */
@Data
public class PaginationMeta {
    /** The number of this page ({@code current_page}). */
    @SerializedName("current_page")
    private Integer currentPage;

    /** The position of the first item of this page ({@code from}). */
    @SerializedName("from")
    private Integer from;

    /** The number of the last page ({@code last_page}). */
    @SerializedName("last_page")
    private Integer lastPage;

    /** The URL of the list, without the page parameters ({@code path}). */
    @SerializedName("path")
    private String path;

    /** The number of items per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;

    /** The position of the last item of this page ({@code to}). */
    @SerializedName("to")
    private Integer to;

    /** The number of items in all the pages ({@code total}). */
    @SerializedName("total")
    private Integer total;
}
