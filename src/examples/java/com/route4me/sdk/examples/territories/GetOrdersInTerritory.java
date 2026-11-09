package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads a territory with its orders with {@link TerritoriesManager#getOrdersInTerritory}. Set {@code TERRITORY_ID}
 * to one of your territories.
 */
public class GetOrdersInTerritory {

    public static void main(String[] args) {
        String territoryId = System.getenv("TERRITORY_ID");
        if (territoryId == null || territoryId.isEmpty()) {
            System.err.println("Set TERRITORY_ID to the ID of one of your territories; this example reads its orders.");
            return;
        }
        try {
            String apiKey = System.getenv("R4M_API_KEY");
            TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);
            Territory responseObject = territoriesManager.getOrdersInTerritory(territoryId);
            System.out.println("Orders:\n" + (responseObject == null ? null : responseObject.getOrders()));
        } catch (APIException ex) {
            Logger.getLogger(GetOrdersInTerritory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
