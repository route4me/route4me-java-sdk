package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class LocationCombinedRequest {
    @SerializedName("filters")
    private LocationFilters filters;

    @SerializedName("order_by")
    private List<String[]> orderBy;

    @SerializedName("page")
    private Integer page;

    @SerializedName("per_page")
    private Integer perPage;

    @SerializedName("group_by")
    private String groupBy;

    @SerializedName("selected_fields")
    private List<String> selectedFields;
}
