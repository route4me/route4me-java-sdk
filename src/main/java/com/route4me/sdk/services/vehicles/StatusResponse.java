package com.route4me.sdk.services.vehicles;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The status answer of several {@link VehiclesManagerV5} methods. */
@Data
public class StatusResponse {
    /** Status of the request process ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
