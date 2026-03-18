package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class PaginationMeta {
    @SerializedName("current_page")
    private Integer currentPage;

    @SerializedName("from")
    private Integer from;

    @SerializedName("last_page")
    private Integer lastPage;

    @SerializedName("path")
    private String path;

    @SerializedName("per_page")
    private Integer perPage;

    @SerializedName("to")
    private Integer to;

    @SerializedName("total")
    private Integer total;
}
