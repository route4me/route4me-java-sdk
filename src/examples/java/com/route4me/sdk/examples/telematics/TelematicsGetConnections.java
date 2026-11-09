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
 * Lists the telematics connections of a member with {@link TelematicsManager#getTelematicsConnections}. The
 * member's telematics API token comes from {@code R4M_TELEMATICS_API_TOKEN}.
 */
public class TelematicsGetConnections {

    public static void main(String[] args) {
        String apiToken = System.getenv("R4M_TELEMATICS_API_TOKEN");
        if (apiToken == null || apiToken.isEmpty()) {
            System.err.println("Set R4M_TELEMATICS_API_TOKEN to the member's telematics API token (from"
                    + " registerMember).");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);
        try {
            List<TelematicsConnection> connections = manager.getTelematicsConnections(apiToken);
            System.out.println(connections);
        } catch (APIException ex) {
            Logger.getLogger(TelematicsGetConnections.class.getName()).log(Level.SEVERE, ex.toString());
        }
    }

}
