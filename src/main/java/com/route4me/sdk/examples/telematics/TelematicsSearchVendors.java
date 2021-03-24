/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsVendorInfo;
import com.route4me.sdk.services.telematics.TelematicsVendorsInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Route4Me
 */
public class TelematicsSearchVendors {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        TelematicsManager manager = new TelematicsManager(apiKey);

        try {
            System.out.println("\nVendors Per Country\n");
            TelematicsVendorsInfo vendors = manager.searchVendorByCountry("GB", "1", "2");
            for (TelematicsVendorInfo vendor : vendors.getVendors()) {
                System.out.println(vendor);

            }

            System.out.println("\nVendors Per Size\n");

            vendors = manager.searchVendorBySize("global", "1", "2");
            for (TelematicsVendorInfo vendor : vendors.getVendors()) {
                System.out.println(vendor);

            }

            System.out.println("\nVendors Per Feature\n");

            vendors = manager.searchVendorByFeature("Satellite", "1", "2");
            for (TelematicsVendorInfo vendor : vendors.getVendors()) {
                System.out.println(vendor);

            }

            System.out.println("\nVendors Per Key Word\n");

            vendors = manager.searchVendorByKeyWord("fleet", "1", "2");
            for (TelematicsVendorInfo vendor : vendors.getVendors()) {
                System.out.println(vendor);

            }

        } catch (APIException ex) {
            Logger.getLogger(TelematicsRegisterMember.class.getName()).log(Level.INFO, ex.toString());
        }

    }

}
