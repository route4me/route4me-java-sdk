package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** A page of locations, in a {@link LocationCombinedResource}. */
@Data
public class LocationCombinedData {
    /** The locations of this page ({@code items}). */
    @SerializedName("items")
    private List<LocationCombinedDataItem> items;

    /** The number of this page ({@code current_page_index}). */
    @SerializedName("current_page_index")
    private Integer currentPageIndex;

    /** The number of locations in all the pages ({@code total_items_count}). */
    @SerializedName("total_items_count")
    private Integer totalItemsCount;

    /** The field the locations are grouped by ({@code group_by}). */
    @SerializedName("group_by")
    private String groupBy;

    /** The columns of the list ({@code columns}). */
    @SerializedName("columns")
    private List<LocationColumn> columns;

    /** The key of the column configuration of the list ({@code columns_configurator_key}). */
    @SerializedName("columns_configurator_key")
    private String columnsConfiguratorKey;
}
