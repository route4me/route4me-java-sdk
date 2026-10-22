package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class LocationTypeCollection {
    @SerializedName("data")
    private List<LocationTypeResource> data;
}
