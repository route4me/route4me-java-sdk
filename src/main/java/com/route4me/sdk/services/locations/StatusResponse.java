package com.route4me.sdk.services.locations;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link LocationManager#exportLocations(LocationExportRequest)}. */
@Data
public class StatusResponse {
    /** Whether the request succeeded ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
