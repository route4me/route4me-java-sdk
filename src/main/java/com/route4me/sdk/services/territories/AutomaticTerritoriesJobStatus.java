package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of {@link TerritoriesManagerV5#getAutomaticTerritoriesJobStatus(String)}. */
@Data
public class AutomaticTerritoriesJobStatus {
    /** The status of the job ({@code status}). */
    @SerializedName("status")
    private String status;
}
