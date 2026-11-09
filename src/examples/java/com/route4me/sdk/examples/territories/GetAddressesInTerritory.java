package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads a territory with its addresses with {@link TerritoriesManager#getAddressesInTerritory}. Set
 * {@code TERRITORY_ID} to one of your territories.
 */
public class GetAddressesInTerritory {

    public static void main(String[] args) {
        String territoryId = System.getenv("TERRITORY_ID");
        if (territoryId == null || territoryId.isEmpty()) {
            System.err.println("Set TERRITORY_ID to the ID of one of your territories; this example reads its"
                    + " addresses.");
            return;
        }
        try {
            String apiKey = System.getenv("R4M_API_KEY");
            TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);
            Territory responseObject = territoriesManager.getAddressesInTerritory(territoryId);
            System.out.println("Addresses:\n" + (responseObject == null ? null : responseObject.getAddresses()));
        } catch (APIException ex) {
            Logger.getLogger(GetAddressesInTerritory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
