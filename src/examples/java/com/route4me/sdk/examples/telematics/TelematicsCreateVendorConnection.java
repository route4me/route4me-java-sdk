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
 * Connects a member to a Geotab account (telematics vendor 4) with
 * {@link TelematicsManager#createTelematicsConnection}. The member's telematics API token and the vendor credentials
 * come from environment variables: {@code R4M_TELEMATICS_API_TOKEN}, {@code R4M_TELEMATICS_ACCOUNT_ID},
 * {@code R4M_TELEMATICS_USERNAME} and {@code R4M_TELEMATICS_PASSWORD}.
 */
public class TelematicsCreateVendorConnection {

    public static void main(String[] args) {
        String apiToken = System.getenv("R4M_TELEMATICS_API_TOKEN");
        String accountId = System.getenv("R4M_TELEMATICS_ACCOUNT_ID");
        String username = System.getenv("R4M_TELEMATICS_USERNAME");
        String password = System.getenv("R4M_TELEMATICS_PASSWORD");
        if (isEmpty(apiToken) || isEmpty(accountId) || isEmpty(username) || isEmpty(password)) {
            System.err.println("Set R4M_TELEMATICS_API_TOKEN to the member's telematics API token (from"
                    + " registerMember), and R4M_TELEMATICS_ACCOUNT_ID, R4M_TELEMATICS_USERNAME and"
                    + " R4M_TELEMATICS_PASSWORD to the credentials of a test account at the vendor.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);
        TelematicsConnection credentials = new TelematicsConnection();

        credentials.setVendorID(4);
        credentials.setAccountID(accountId);
        credentials.setUsername(username);
        credentials.setPassword(password);
        credentials.setVehiclePositionRefreshRate(60);
        credentials.setName("Test Connection from Java SDK");
        credentials.setValidateRemoteCredentials("false");
        try {

            TelematicsConnection connection = manager.createTelematicsConnection(apiToken, credentials);
            System.out.println(connection);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TelematicsCreateVendorConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIException ex) {
            Logger.getLogger(TelematicsCreateVendorConnection.class.getName()).log(Level.SEVERE, ex.toString());
        }

    }

    private static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

}
