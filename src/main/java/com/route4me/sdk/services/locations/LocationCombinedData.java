package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class LocationCombinedData {
    @SerializedName("items")
    private List<LocationCombinedDataItem> items;

    @SerializedName("current_page_index")
    private Integer currentPageIndex;

    @SerializedName("total_items_count")
    private Integer totalItemsCount;

    @SerializedName("group_by")
    private String groupBy;

    @SerializedName("columns")
    private List<LocationColumn> columns;

    @SerializedName("columns_configurator_key")
    private String columnsConfiguratorKey;
}
