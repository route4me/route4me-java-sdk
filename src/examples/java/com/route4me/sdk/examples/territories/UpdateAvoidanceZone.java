package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;
import com.route4me.sdk.services.territories.TerritoryData;
import java.util.Arrays;

/**
 * Turns an avoidance zone into a 5 km circle with {@link TerritoriesManager#updateAvoidanceZone}. Set
 * {@code TERRITORY_ID} to the ID of one of your avoidance zones.
 */
public class UpdateAvoidanceZone {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManager manager = new TerritoriesManager(apiKey);
        TerritoryData shape = new TerritoryData();
        shape.setType(Constants.TerritoryType.CIRCLE.toString());
        shape.setData(Arrays.asList("37.569752822786455,-77.47833251953125", "5000"));
        Territory zone = new Territory();
        zone.setTerritoryId(System.getenv("TERRITORY_ID"));
        zone.setTerritoryName("Circle avoidance zone");
        zone.setTerritoryColor("ff0000");
        zone.setTerritory(shape);
        try {
            Territory updated = manager.updateAvoidanceZone(zone);
            System.out.println("Updated zone: " + updated.getTerritoryName());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
