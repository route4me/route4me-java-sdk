package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The column configuration of a {@link LocationCombinedResource}. */
@Data
public class LocationConfig {
    /** Array of column definitions for display ({@code columns}). */
    @SerializedName("columns")
    private List<LocationColumn> columns;

    /** Key identifying the column configuration ({@code columns_configurator_key}). */
    @SerializedName("columns_configurator_key")
    private String columnsConfiguratorKey;
}
