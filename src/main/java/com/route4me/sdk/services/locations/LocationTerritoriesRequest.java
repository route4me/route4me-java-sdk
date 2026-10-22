package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationTerritoriesRequest extends LocationCombinedRequest {
    @SerializedName("territories_page")
    private Integer territoriesPage;

    @SerializedName("territories_per_page")
    private Integer territoriesPerPage;

    @SerializedName("territories_search_query")
    private String territoriesSearchQuery;

    @SerializedName("territories_order_by")
    private List<String[]> territoriesOrderBy;
}
