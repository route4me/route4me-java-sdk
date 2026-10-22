package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsVendorFeatures;
import com.route4me.sdk.services.telematics.TelematicsVendorInfo;
import com.route4me.sdk.services.telematics.TelematicsVendorsV5Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Compares the features of three telematics vendors (IDs 1, 50 and 52): it reads the vendor catalog with
 * {@link TelematicsManager#getTelematicsVendorsV5()}
 * ({@code GET https://wh.route4me.com/modules/api/v5.0/telematics/vendors/all}) and prints the features of
 * each of them.
 */
public class TelematicsVendorsFeturesComparison {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);
        List<String> vendorIds = Arrays.asList("1", "50", "52");
        try {
            TelematicsVendorsV5Response catalog = manager.getTelematicsVendorsV5();
            for (TelematicsVendorInfo vendor : catalog.getData()) {
                if (!vendorIds.contains(vendor.getVendorID())) {
                    continue;
                }
                List<String> features = new ArrayList<>();
                if (vendor.getFeatures() != null) {
                    for (TelematicsVendorFeatures feature : vendor.getFeatures()) {
                        features.add(feature.getName());
                    }
                }
                System.out.println(vendor.getName() + ": " + String.join(", ", features));
            }
        } catch (APIException ex) {
            Logger.getLogger(TelematicsVendorsFeturesComparison.class.getName()).log(Level.INFO, ex.toString());
        }
    }
}
