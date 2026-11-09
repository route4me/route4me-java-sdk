// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;

/**
 * Reads an avoidance zone with {@link TerritoriesManager#getAvoidanceZone}. Set {@code TERRITORY_ID} to one of your
 * avoidance zones.
 */
public class GetAvoidanceZone {

    public static void main(String[] args) throws APIException {
        String territoryId = System.getenv("TERRITORY_ID");
        if (territoryId == null || territoryId.isEmpty()) {
            System.err.println("Set TERRITORY_ID to the ID of one of your avoidance zones; this example reads it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);
        Territory responseObject = territoriesManager.getAvoidanceZone(territoryId);
        System.out.println(responseObject);
    }

}
// codebeat:enable[SIMILARITY]
