/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsMemberRegister;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Registers a member for telematics with {@link TelematicsManager#registerMember}, which returns the telematics
 * API token of the member. Set {@code MEMBER_ID} to a member of a test account: the example registers it. It does not
 * print the token: read it with {@code getApiToken()} and set {@code R4M_TELEMATICS_API_TOKEN} to it for the other
 * telematics examples.
 */
public class TelematicsRegisterMember {

    public static void main(String[] args) {
        String memberId = System.getenv("MEMBER_ID");
        if (memberId == null || memberId.isEmpty()) {
            System.err.println("Set MEMBER_ID to the ID of a member of a test account; this example registers it for"
                    + " telematics.");
            return;
        }
        Integer id = ExampleInput.toInteger("MEMBER_ID", memberId);
        if (id == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);

        try {
            TelematicsMemberRegister register = manager.registerMember(id);
            System.out.println(register);
            // The token is a credential, so the example does not print it.
            System.out.println(register != null && register.getApiToken() != null
                    ? "Telematics API token received: read it with getApiToken(), and set R4M_TELEMATICS_API_TOKEN"
                    + " to it."
                    : "The answer has no telematics API token.");
        } catch (APIException ex) {
            Logger.getLogger(TelematicsRegisterMember.class.getName()).log(Level.SEVERE, ex.toString());
        }
    }

}
