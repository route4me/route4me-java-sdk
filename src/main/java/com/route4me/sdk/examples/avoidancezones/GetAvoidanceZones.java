/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.avoidancezones;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.AvoidanceZoneManager;
import com.route4me.sdk.model.Territory;
import java.util.List;

/**
 *
 * @author juan
 */
public class GetAvoidanceZones {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        AvoidanceZoneManager avoidanceZoneManager = route4me.getAvoidanceZoneManager();
        List<Territory> territories = avoidanceZoneManager.getAvoidanceZones();
        for (Territory territory : territories) {
            System.out.println("Territory ID: " + territory.getTerritory_id());
            System.out.println("Territory Name: " + territory.getTerritory_name());
            System.out.println("Territory Color: " + territory.getTerritory_color());
            System.out.println("Territory Type: " + territory.getTerritory().getType());
            System.out.println();
        }

    }

}
