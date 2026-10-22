package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class LocationConfig {
    @SerializedName("columns")
    private List<LocationColumn> columns;

    @SerializedName("columns_configurator_key")
    private String columnsConfiguratorKey;
}
