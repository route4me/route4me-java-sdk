package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;
import com.route4me.sdk.services.territories.TerritoryData;
import java.util.Arrays;

/**
 * Turns a territory into a rectangle with {@link TerritoriesManager#updateTerritory}. Set {@code TERRITORY_ID} to the
 * ID of one of your territories.
 */
public class UpdateTerritory {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManager manager = new TerritoriesManager(apiKey);
        TerritoryData shape = new TerritoryData();
        shape.setType(Constants.TerritoryType.RECT.toString());
        shape.setData(Arrays.asList("43.51668853502909,-109.3798828125", "46.98025235521883,-101.865234375"));
        Territory territory = new Territory();
        territory.setTerritoryId(System.getenv("TERRITORY_ID"));
        territory.setTerritoryName("Rectangle territory");
        territory.setTerritoryColor("00ff00");
        territory.setTerritory(shape);
        try {
            Territory updated = manager.updateTerritory(territory);
            System.out.println("Updated territory: " + updated.getTerritoryName());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
