package com.route4me.sdk.services.facilities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

/** The answer of {@link FacilityManager#getFacilityTypes()}. */
@Data
public class FacilityTypeCollectionResource {
    /** Array of facility types ({@code data}). */
    @SerializedName("data")
    private List<FacilityTypeResource> data;
}
