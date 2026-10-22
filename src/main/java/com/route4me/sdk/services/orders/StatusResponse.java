package com.route4me.sdk.services.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The status answer of several order methods. */
@Data
public class StatusResponse {
    /** Status of the request process ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
