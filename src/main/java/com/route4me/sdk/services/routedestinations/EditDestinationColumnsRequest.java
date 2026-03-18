package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class EditDestinationColumnsRequest {
    @SerializedName("columns_configurator_key")
    private String columnsConfiguratorKey;

    @SerializedName("order")
    private List<String> order;
}
