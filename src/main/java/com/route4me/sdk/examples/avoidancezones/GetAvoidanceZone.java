
package com.route4me.sdk.examples.avoidancezones;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.zones.AvoidanceZoneManager;
import com.route4me.sdk.services.zones.Territory;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetAvoidanceZone {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        AvoidanceZoneManager avoidanceZoneManager = route4me.getAvoidanceZoneManager();
        List<Territory> territories = avoidanceZoneManager.getAvoidanceZones();
        String territoryID;
        territoryID = territories.get(0).getTerritory_id();
        Territory responseOject = avoidanceZoneManager.getAvoidanceZone(territoryID);
        System.out.println("Territory ID: " + responseOject.getTerritory_id());
        System.out.println("Territory Name: " + responseOject.getTerritory_name());
        System.out.println("Territory Color: " + responseOject.getTerritory_color());
        System.out.println("Territory Type: " + responseOject.getTerritory().getType());
        System.out.println();        
    }

}
