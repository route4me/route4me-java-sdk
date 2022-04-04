/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsVendorsInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Route4Me
 */
public class TelematicsGetVendorInfo {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);

        try {
            TelematicsVendorsInfo vendor = manager.getTelematicsVendorInfo("3");
            System.out.println(vendor.getVendor());

        } catch (APIException ex) {
            Logger.getLogger(TelematicsRegisterMember.class.getName()).log(Level.INFO, ex.toString());
        }

    }

}
