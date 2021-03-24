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
 *
 * @author Route4Me
 */
public class TelematicsRegisterMember {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        TelematicsManager manager = new TelematicsManager(apiKey);

        try {
            TelematicsMemberRegister register = manager.registerMember(1);
            System.out.println(register);
        } catch (APIException ex) {
            Logger.getLogger(TelematicsRegisterMember.class.getName()).log(Level.INFO, ex.toString());
        }
    }

}
