package com.route4me.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of the V4 methods that return only a status. */
@Data
public class StatusResponse {
    /** Whether the request succeeded ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
