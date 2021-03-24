/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsVendorComparison;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Route4Me
 */
public class TelematicsVendorsFeturesComparison {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        TelematicsManager manager = new TelematicsManager(apiKey);

        try {
            List<TelematicsVendorComparison> features = manager.compareTelematicsVendors("1,50,52");
            for (TelematicsVendorComparison c : features) {
                System.out.println(c);
            }
        } catch (APIException ex) {
            Logger.getLogger(TelematicsVendorsFeturesComparison.class.getName()).log(Level.INFO, ex.toString());
        }
    }

}
