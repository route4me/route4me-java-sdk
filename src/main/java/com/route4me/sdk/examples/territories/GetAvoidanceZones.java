package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;

import java.util.List;

/**
 * @author juan
 */
public class GetAvoidanceZones {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);
        try {
            List<Territory> territories = territoriesManager.getAvoidanceZones();
            for (Territory territory : territories) {
                System.out.println(territory);
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
