
package com.route4me.sdk.examples.avoidancezones;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.zones.AvoidanceZoneManager;
import com.route4me.sdk.services.zones.Territory;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetAvoidanceZone {

    public static void main(String[] args) throws APIException {
        String apiKey = "11111111111111111111111111111111";
        AvoidanceZoneManager avoidanceZoneManager = new AvoidanceZoneManager(apiKey);;
        List<Territory> territories = avoidanceZoneManager.getAvoidanceZones();
        String territoryID;
        territoryID = territories.get(0).getTerritoryId();
        Territory responseObject = avoidanceZoneManager.getAvoidanceZone(territoryID);
        System.out.println(responseObject);
    }

}
