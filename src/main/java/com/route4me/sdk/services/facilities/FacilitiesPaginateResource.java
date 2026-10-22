package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class FacilitiesPaginateResource {
    @SerializedName("data")
    private List<FacilityResource> data;

    @SerializedName("current_page")
    private Integer currentPage;

    @SerializedName("total")
    private Integer total;

    @SerializedName("per_page")
    private Integer perPage;

    @SerializedName("last_page")
    private Integer lastPage;

    @SerializedName("next_page_url")
    private String nextPageUrl;

    @SerializedName("prev_page_url")
    private String prevPageUrl;

    @SerializedName("from")
    private Integer from;

    @SerializedName("to")
    private Integer to;
}
