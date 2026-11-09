package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The body of {@link RouteDestinationsManager#editDestinationColumns(EditDestinationColumnsRequest)}. */
@Data
public class EditDestinationColumnsRequest {
    /** The configuration key that identifies which column set to update ({@code columns_configurator_key}). */
    @SerializedName("columns_configurator_key")
    private String columnsConfiguratorKey;

    /** Ordered array of column ID strings representing the desired column order ({@code order}). */
    @SerializedName("order")
    private List<String> order;
}
