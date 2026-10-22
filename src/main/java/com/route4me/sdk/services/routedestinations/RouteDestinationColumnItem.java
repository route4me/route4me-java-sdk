package com.route4me.sdk.services.routedestinations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RouteDestinationColumnItem {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;
}
