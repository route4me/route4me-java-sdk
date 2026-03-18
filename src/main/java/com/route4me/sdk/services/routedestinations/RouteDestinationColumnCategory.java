package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteDestinationColumnCategory {
    @SerializedName("category")
    private String category;

    @SerializedName("columns")
    private List<RouteDestinationColumnItem> columns;
}
