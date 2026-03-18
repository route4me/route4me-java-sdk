package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class DriverReviewsResponse {
    @SerializedName("data")
    private List<DriverReview> data;

    @SerializedName("simple_pagination")
    private SimplePaginationData simplePagination;

    @SerializedName("total")
    private List<TypeQuantity> total;
}
