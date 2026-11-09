package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The status answer of several {@link SchedulesManager} methods. */
@Data
public class StatusResponse {
    /** Whether the request succeeded ({@code status}). */
    @SerializedName("status")
    private Boolean status;
}
