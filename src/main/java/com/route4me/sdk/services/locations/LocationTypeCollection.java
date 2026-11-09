package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link LocationManager#getLocationTypes(Integer, Integer)}. */
@Data
public class LocationTypeCollection {
    /** Array of location type resources ({@code data}). */
    @SerializedName("data")
    private List<LocationTypeResource> data;
}
