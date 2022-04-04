package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author juan
 */
public class GetAddressesInTerritory {

    public static void main(String[] args) {
        try {
            String apiKey = System.getenv("R4M_API_KEY");
            TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);
            List<Territory> territories = territoriesManager.getTerritories();
            String territoryID;
            territoryID = territories.get(0).getTerritoryId();
            Territory responseObject = territoriesManager.getAddressesInTerritory(territoryID);
            System.out.println("Addresses:\n" + responseObject.getAddresses());
        } catch (APIException ex) {
            Logger.getLogger(GetAddressesInTerritory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
