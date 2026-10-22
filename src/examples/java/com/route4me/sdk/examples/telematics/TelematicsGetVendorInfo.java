package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsVendorV5;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gets one telematics vendor, with its sync entities and credential fields, with
 * {@link TelematicsManager#getTelematicsVendorV5(long)}
 * ({@code GET https://wh.route4me.com/modules/api/v5.0/telematics/vendors?vendor_id=...}).
 */
public class TelematicsGetVendorInfo {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);
        try {
            TelematicsVendorV5 vendor = manager.getTelematicsVendorV5(3L);
            System.out.println(vendor);
        } catch (APIException ex) {
            Logger.getLogger(TelematicsGetVendorInfo.class.getName()).log(Level.INFO, ex.toString());
        }
    }
}
