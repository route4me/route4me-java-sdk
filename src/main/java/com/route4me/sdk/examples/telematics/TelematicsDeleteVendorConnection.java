/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsConnection;
import com.route4me.sdk.services.telematics.TelematicsManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Route4Me
 */
public class TelematicsDeleteVendorConnection {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        TelematicsManager manager = new TelematicsManager(apiKey);
        try {

            TelematicsConnection connection = manager.deleteTelematicsConnection("a8a60c1fccb5db60bbb9ca6a6ff71aef", "7DltI9rWz11HAZPL4tZPL4tz4pthRPLu3AvuM");
            System.out.println(connection);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TelematicsDeleteVendorConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIException ex) {
            Logger.getLogger(TelematicsDeleteVendorConnection.class.getName()).log(Level.SEVERE, ex.toString());
        }

    }

}
