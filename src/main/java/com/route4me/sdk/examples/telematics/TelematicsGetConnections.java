/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsConnection;
import com.route4me.sdk.services.telematics.TelematicsManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Route4Me
 */
public class TelematicsGetConnections {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);
        try {
            List<TelematicsConnection> connections = manager.getTelematicsConnections("a8a60c1fccb5db60bbb9ca6a6ff71aef");
            System.out.println(connections);
        } catch (APIException ex) {
            Logger.getLogger(TelematicsGetConnections.class.getName()).log(Level.SEVERE, ex.toString());
        }
    }

}
