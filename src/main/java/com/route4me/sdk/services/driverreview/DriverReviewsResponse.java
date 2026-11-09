package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link DriverReviewManager#getDriverReviews(Integer, Long, String, String, Integer, Integer)}. */
@Data
public class DriverReviewsResponse {
    /** The reviews of this page ({@code data}). */
    @SerializedName("data")
    private List<DriverReview> data;

    /** The links to the other pages ({@code simple_pagination}). */
    @SerializedName("simple_pagination")
    private SimplePaginationData simplePagination;

    /** The number of reviews per rating ({@code total}). */
    @SerializedName("total")
    private List<TypeQuantity> total;
}
