package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;
import com.route4me.sdk.services.territories.TerritoryData;

import java.util.ArrayList;
import java.util.List;

public class DeleteAvoidanceZone {

    public static void main(String[] args) throws APIException {
        String apiKey = "11111111111111111111111111111111";
        TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);
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
        polyTerritory.setTerritoryName("Polygon Territory");
        polyTerritory.setTerritoryColor("ff0000");
        polyTerritory.setTerritory(polyTerritoryData);
        polyTerritoryData.setType(Constants.TerritoryType.POLY.toString());
        Territory responseObject = territoriesManager.addAvoidanceZone(polyTerritory);

        System.out.println("Polygon Territory");
        System.out.println(responseObject.toString());
        System.out.println();

        System.out.println("Deleting Territory");
        boolean response = territoriesManager.deleteAvoidanceZone(responseObject.getTerritoryId());
        System.out.println(response);
    }

}



    
