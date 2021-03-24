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
public class TelematicsCreateVendorConnection {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        TelematicsManager manager = new TelematicsManager(apiKey);
        TelematicsConnection credentials = new TelematicsConnection();

        credentials.setVendorID(4);
        credentials.setAccountID("12345");
        credentials.setUsername("username");
        credentials.setPassword("password");
        credentials.setVehiclePositionRefreshRate(60);
        credentials.setName("Test Connection from Java SDK");
        credentials.setValidateRemoteCredentials("false");
        try {

            TelematicsConnection connection = manager.createTelematicsConnection("a8a60c1fccb5db60bbb9ca6a6ff71aef", credentials);
            System.out.println(connection);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TelematicsCreateVendorConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIException ex) {
            Logger.getLogger(TelematicsCreateVendorConnection.class.getName()).log(Level.SEVERE, ex.toString());
        }

    }

}
