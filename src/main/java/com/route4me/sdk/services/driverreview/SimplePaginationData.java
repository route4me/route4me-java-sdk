package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SimplePaginationData {
    @SerializedName("per_page")
    private Integer perPage;

    @SerializedName("current_page")
    private Integer currentPage;

    @SerializedName("path")
    private String path;

    @SerializedName("first")
    private String first;

    @SerializedName("prev")
    private String previous;

    @SerializedName("next")
    private String next;
}
