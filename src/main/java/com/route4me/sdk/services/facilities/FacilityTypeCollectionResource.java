package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class FacilityTypeCollectionResource {
    @SerializedName("data")
    private List<FacilityTypeResource> data;
}
