/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsMemberRegister;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Registers a member for telematics with {@link TelematicsManager#registerMember}, which returns the telematics
 * API token of the member. Set {@code MEMBER_ID} to a member of the account.
 */
public class TelematicsRegisterMember {

    public static void main(String[] args) {
        String memberId = System.getenv("MEMBER_ID");
        if (memberId == null || memberId.isEmpty()) {
            System.err.println("Set MEMBER_ID to the ID of a member of the account; this example registers it for"
                    + " telematics.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);

        try {
            TelematicsMemberRegister register = manager.registerMember(Integer.valueOf(memberId));
            System.out.println(register);
        } catch (APIException ex) {
            Logger.getLogger(TelematicsRegisterMember.class.getName()).log(Level.SEVERE, ex.toString());
        }
    }

}
