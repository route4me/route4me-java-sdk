package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Direction {

    @SerializedName("location")
    private Location location;
    @SerializedName("steps")
    private List<Step> steps;
}
