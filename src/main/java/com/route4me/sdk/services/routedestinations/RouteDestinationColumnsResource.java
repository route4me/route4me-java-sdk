package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteDestinationColumnsResource {
    @SerializedName("categories")
    private List<RouteDestinationColumnCategory> categories;

    @SerializedName("order")
    private List<String> order;

    @SerializedName("default_order")
    private List<String> defaultOrder;
}
