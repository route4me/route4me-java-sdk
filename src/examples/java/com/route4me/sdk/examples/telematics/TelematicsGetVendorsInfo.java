package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsVendorInfo;
import com.route4me.sdk.services.telematics.TelematicsVendorsV5Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lists the whole telematics vendor catalog with {@link TelematicsManager#getTelematicsVendorsV5()}
 * ({@code GET https://wh.route4me.com/modules/api/v5.0/telematics/vendors/all}).
 */
public class TelematicsGetVendorsInfo {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);

        try {
            TelematicsVendorsV5Response vendors = manager.getTelematicsVendorsV5();
            if (vendors == null || vendors.getData() == null) {
                System.out.println("No vendors");
                return;
            }
            for (TelematicsVendorInfo vendor : vendors.getData()) {
                System.out.println(vendor);
            }
        } catch (APIException ex) {
            Logger.getLogger(TelematicsGetVendorsInfo.class.getName()).log(Level.INFO, ex.toString());
        }
    }
}
