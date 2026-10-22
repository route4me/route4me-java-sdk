package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/** The answer of the V5 list methods of {@link TerritoriesManagerV5}: territories or avoidance zones. */
@Data
public class TerritoryResults {
    /** The territories or avoidance zones ({@code results}). */
    @SerializedName("results")
    private List<Territory> results;
}
