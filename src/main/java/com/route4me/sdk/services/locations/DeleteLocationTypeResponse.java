package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link LocationManager#deleteLocationType(String)}. */
@Data
public class DeleteLocationTypeResponse {
    /** Indicates whether the deletion was successful ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
