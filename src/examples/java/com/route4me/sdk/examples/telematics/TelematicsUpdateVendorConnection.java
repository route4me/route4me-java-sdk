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

/** Updates a telematics connection with {@link TelematicsManager#updateTelematicsConnection}. The
 * tokens come from environment variables. */
public class TelematicsUpdateVendorConnection {

    public static void main(String[] args) {
        String apiToken = System.getenv("R4M_TELEMATICS_API_TOKEN");
        String connectionToken = System.getenv("R4M_TELEMATICS_CONNECTION_TOKEN");
        if (apiToken == null || apiToken.isEmpty() || connectionToken == null || connectionToken.isEmpty()) {
            System.err.println("Set R4M_TELEMATICS_API_TOKEN to the member's telematics API token (from registerMember)"
                    + " and R4M_TELEMATICS_CONNECTION_TOKEN to the token of the connection.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);
        TelematicsConnection credentials = new TelematicsConnection();

        credentials.setVehiclePositionRefreshRate(65);
        credentials.setValidateRemoteCredentials("false");
        try {

            TelematicsConnection connection = manager.updateTelematicsConnection(apiToken, connectionToken,
                    credentials);
            System.out.println(connection);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TelematicsUpdateVendorConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIException ex) {
            Logger.getLogger(TelematicsUpdateVendorConnection.class.getName()).log(Level.SEVERE, ex.toString());
        }

    }

}
