package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteDestinationsListResponse {
    @SerializedName("items")
    private List<RouteDestinationListResource> items;
}
