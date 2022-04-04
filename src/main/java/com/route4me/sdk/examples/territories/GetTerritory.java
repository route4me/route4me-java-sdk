// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;

import java.util.List;

/**
 *
 * @author juan
 */
public class GetTerritory {

    public static void main(String[] args) throws APIException {
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);;
        List<Territory> territories = territoriesManager.getTerritories();
        String territoryID;
        territoryID = territories.get(0).getTerritoryId();
        Territory responseObject = territoriesManager.getTerritory(territoryID);
        System.out.println(responseObject);
    }

}
// codebeat:enable[SIMILARITY]
