package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteDestinationsByOrderResponse {
    @SerializedName("items")
    private List<RouteDestinationResource> items;
}
