/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsVendor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Route4Me
 */
public class TelematicsVendors {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        TelematicsManager manager = new TelematicsManager(apiKey);
        try {
            TelematicsVendor vendors[] = manager.getTelematicsVendor("a8a60c1fccb5db60bbb9ca6a6ff71aef");
            for (TelematicsVendor vendor : vendors) {
                System.out.println(vendor);
            }
        } catch (APIException ex) {
            Logger.getLogger(TelematicsGetConnections.class.getName()).log(Level.SEVERE, ex.toString());
        }

    }

}
