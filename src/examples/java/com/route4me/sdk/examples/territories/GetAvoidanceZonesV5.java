package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.TerritoriesManagerV5;
import com.route4me.sdk.services.territories.Territory;
import com.route4me.sdk.services.territories.TerritoryResults;

/** Lists the avoidance zones through the V5 API with {@link TerritoriesManagerV5#getAvoidanceZones}. */
public class GetAvoidanceZonesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManagerV5 manager = new TerritoriesManagerV5(apiKey);
        try {
            TerritoryResults zones = manager.getAvoidanceZones(null, null, null, null);
            if (zones.getResults() != null) {
                for (Territory zone : zones.getResults()) {
                    System.out.println(zone.getTerritoryId() + ": " + zone.getTerritoryName());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
