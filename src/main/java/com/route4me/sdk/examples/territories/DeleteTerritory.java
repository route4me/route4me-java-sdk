package com.route4me.sdk.examples.territories;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.territories.TerritoriesManager;
import com.route4me.sdk.services.territories.Territory;
import com.route4me.sdk.services.territories.TerritoryData;

import java.util.ArrayList;
import java.util.List;

public class DeleteTerritory {

    public static void main(String[] args) throws APIException {
        String apiKey = "11111111111111111111111111111111";
        TerritoriesManager territoriesManager = new TerritoriesManager(apiKey);
        List<String> polyTerritoryDataList = new ArrayList<>();
        polyTerritoryDataList.add("56.1271841561311,56.931152344");
        polyTerritoryDataList.add("58.413222590568,59.5019531");
        polyTerritoryDataList.add("61.538406167167,59.3151855469");
        polyTerritoryDataList.add("61.0476505860311,51.9982910156");
        polyTerritoryDataList.add("59.2546495444837,53.635253906");
        polyTerritoryDataList.add("56.47462805805596,54.42626953");
        TerritoryData polyTerritoryData = new TerritoryData();
        polyTerritoryData.setData(polyTerritoryDataList);
        Territory polyTerritory = new Territory();
        polyTerritory.setTerritoryName("Polygon Territory");
        polyTerritory.setTerritoryColor("ff0001");
        polyTerritory.setTerritory(polyTerritoryData);
        polyTerritoryData.setType(Constants.TerritoryType.POLY.toString());
        Territory responseObject = territoriesManager.addTerritory(polyTerritory);

        System.out.println("Polygon Territory");
        System.out.println(responseObject.toString());
        System.out.println();

        System.out.println("Deleting Territory");
        boolean response = territoriesManager.deleteTerritory(responseObject.getTerritoryId());
        System.out.println(response);
    }

}