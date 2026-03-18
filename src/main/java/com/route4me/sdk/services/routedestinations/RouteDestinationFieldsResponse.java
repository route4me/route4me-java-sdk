package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RouteDestinationFieldsResponse {
    @SerializedName("fields")
    private List<RouteDestinationFieldItem> fields;
}
