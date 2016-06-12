
package com.route4me.sdk.examples.avoidancezones;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.zones.AvoidanceZoneManager;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.services.zones.Territory;
import com.route4me.sdk.services.zones.TerritoryData;
import com.route4me.sdk.model.enums.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juan
 */
public class DeleteAvoidanceZone {




    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        AvoidanceZoneManager avoidanceZoneManager = route4me.getAvoidanceZoneManager();
        List<String> polyTerritoryDataList = new ArrayList<>();
        polyTerritoryDataList.add("56.127184156131065,56.93115234375");
        polyTerritoryDataList.add("58.41322259056806,59.501953125");
        polyTerritoryDataList.add("61.53840616716746,59.315185546875");
        polyTerritoryDataList.add("61.047650586031104,51.998291015625");
        polyTerritoryDataList.add("59.254649544483726,53.63525390625");
        polyTerritoryDataList.add("56.47462805805596,54.42626953125");
        TerritoryData polyTerritoryData = new TerritoryData();
        polyTerritoryData.setData(polyTerritoryDataList);
        Territory polyTerritory = new Territory();
        polyTerritory.setTerritory_name("Polygon Territory");
        polyTerritory.setTerritory_color("ff0000");
        polyTerritory.setTerritory(polyTerritoryData);
        polyTerritoryData.setType(Constants.TerritoryType.POLY.toString());
        Territory responseOject = avoidanceZoneManager.addAvoidanceZone(polyTerritory);

        System.out.println("Polygon Territory");
        System.out.println("Territory ID: " + responseOject.getTerritory_id());
        System.out.println("Territory Name: " + responseOject.getTerritory_name());
        System.out.println("Territory Color: " + responseOject.getTerritory_color());
        System.out.println("Territory Type: " + responseOject.getTerritory().getType());
        System.out.println();
        
        System.out.println("Deleting Territory");
        Response response;
        response = avoidanceZoneManager.deleteAvoidanceZone(responseOject.getTerritory_id());
        System.err.println(response.getResponseBody());
            
        

    }
    
}



    
