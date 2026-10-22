package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsVendorInfo;
import com.route4me.sdk.services.telematics.TelematicsVendorsV5Response;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Compares some telematics vendors side by side: it reads the vendor catalog with
 * {@link TelematicsManager#getTelematicsVendorsV5()}
 * ({@code GET https://wh.route4me.com/modules/api/v5.0/telematics/vendors/all}) and prints, for each vendor ID it is
 * given, the name, whether Route4Me integrates it ({@code is_integrated}), its size and its website. The catalog has no
 * feature list per vendor; filter the catalog by feature with the {@code feature} of a
 * {@code TelematicsVendorsV5Request} instead (see {@code TelematicsSearchVendors}).
 *
 * <p>Give the vendor IDs of the V5 catalog as arguments, or in {@code TELEMATICS_VENDOR_IDS}, separated by commas.
 */
public class TelematicsVendorsFeturesComparison {

    public static void main(String[] args) {
        Set<String> vendorIds = new LinkedHashSet<>();
        List<String> sources = new ArrayList<>();
        for (String arg : args) {
            sources.add(arg);
        }
        if (sources.isEmpty() && System.getenv("TELEMATICS_VENDOR_IDS") != null) {
            sources.add(System.getenv("TELEMATICS_VENDOR_IDS"));
        }
        for (String source : sources) {
            for (String id : source.split(",")) {
                if (!id.trim().isEmpty()) {
                    vendorIds.add(id.trim());
                }
            }
        }
        if (vendorIds.isEmpty()) {
            System.err.println("Give the vendor IDs to compare as arguments, or in TELEMATICS_VENDOR_IDS (comma"
                    + " separated); TelematicsGetVendorsInfo lists the catalog with its IDs.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);
        try {
            TelematicsVendorsV5Response catalog = manager.getTelematicsVendorsV5();
            if (catalog == null || catalog.getData() == null) {
                System.out.println("The vendor catalog is empty");
                return;
            }
            Set<String> found = new LinkedHashSet<>();
            for (TelematicsVendorInfo vendor : catalog.getData()) {
                if (vendor == null || !vendorIds.contains(vendor.getVendorID())) {
                    continue;
                }
                found.add(vendor.getVendorID());
                System.out.println(vendor.getVendorID() + " " + vendor.getName() + ": integrated "
                        + vendor.getIsIntegrated() + ", size " + vendor.getSize() + ", " + vendor.getWebsiteURL());
            }
            for (String id : vendorIds) {
                if (!found.contains(id)) {
                    System.out.println(id + ": not in the catalog");
                }
            }
        } catch (APIException ex) {
            Logger.getLogger(TelematicsVendorsFeturesComparison.class.getName()).log(Level.INFO, ex.toString());
        }
    }
}
