package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.TerritoriesManagerV5;
import com.route4me.sdk.services.territories.TerritoriesQuery;
import com.route4me.sdk.services.territories.Territory;
import com.route4me.sdk.services.territories.TerritoryResults;

/**
 * Lists the territories with their address and order counts through the V5 API with
 * {@link TerritoriesManagerV5#getTerritories}.
 */
public class GetTerritoriesV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManagerV5 manager = new TerritoriesManagerV5(apiKey);
        TerritoriesQuery query = new TerritoriesQuery().setAddressesCount(true).setOrdersCount(true);
        try {
            TerritoryResults territories = manager.getTerritories(query);
            if (territories.getResults() != null) {
                for (Territory territory : territories.getResults()) {
                    System.out.println(territory.getTerritoryName() + ": " + territory.getAddressesCount()
                            + " addresses, " + territory.getOrdersCount() + " orders");
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
