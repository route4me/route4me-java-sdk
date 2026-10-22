package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The page links of a {@link DriverReviewsResponse}. */
@Data
public class SimplePaginationData {
    /** The number of reviews per page ({@code per_page}). */
    @SerializedName("per_page")
    private Integer perPage;

    /** The number of this page ({@code current_page}). */
    @SerializedName("current_page")
    private Integer currentPage;

    /** The URL of the list, without the page parameters ({@code path}). */
    @SerializedName("path")
    private String path;

    /** The URL of the first page ({@code first}). */
    @SerializedName("first")
    private String first;

    /** The URL of the previous page, or {@code null} on the first page ({@code prev}). */
    @SerializedName("prev")
    private String previous;

    /** The URL of the next page, or {@code null} on the last page ({@code next}). */
    @SerializedName("next")
    private String next;
}
