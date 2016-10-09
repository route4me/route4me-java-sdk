package com.route4me.sdk.examples.avoidancezones;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.zones.AvoidanceZoneManager;
import com.route4me.sdk.services.zones.Territory;

import java.util.List;

/**
 * @author juan
 */
public class GetAvoidanceZones {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        AvoidanceZoneManager avoidanceZoneManager = new AvoidanceZoneManager(apiKey);
        try {
            List<Territory> territories = avoidanceZoneManager.getAvoidanceZones();
            for (Territory territory : territories) {
                System.out.println(territory);
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
