package com.route4me.sdk.examples.telematics;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.telematics.TelematicsManager;
import com.route4me.sdk.services.telematics.TelematicsVendorInfo;
import com.route4me.sdk.services.telematics.TelematicsVendorsV5Request;
import com.route4me.sdk.services.telematics.TelematicsVendorsV5Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Searches the telematics vendor catalog by country, size, feature and keyword, two vendors at a time, with
 * {@link TelematicsManager#getTelematicsVendorsV5(TelematicsVendorsV5Request)}
 * ({@code GET https://wh.route4me.com/modules/api/v5.0/telematics/vendors/all}).
 */
public class TelematicsSearchVendors {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TelematicsManager manager = new TelematicsManager(apiKey);
        try {
            System.out.println("\nVendors Per Country\n");
            print(manager.getTelematicsVendorsV5(new TelematicsVendorsV5Request().setCountry("GB")
                    .setPage(1).setPerPage(2)));

            System.out.println("\nVendors Per Size\n");
            print(manager.getTelematicsVendorsV5(new TelematicsVendorsV5Request().setSize("global")
                    .setPage(1).setPerPage(2)));

            System.out.println("\nVendors Per Feature\n");
            print(manager.getTelematicsVendorsV5(new TelematicsVendorsV5Request().setFeature("Satellite")
                    .setPage(1).setPerPage(2)));

            System.out.println("\nVendors Per Key Word\n");
            print(manager.getTelematicsVendorsV5(new TelematicsVendorsV5Request().setSearch("fleet")
                    .setPage(1).setPerPage(2)));
        } catch (APIException ex) {
            Logger.getLogger(TelematicsSearchVendors.class.getName()).log(Level.INFO, ex.toString());
        }
    }

    /**
     * Prints each vendor of a search.
     *
     * @param vendors the answer of the search
     */
    private static void print(TelematicsVendorsV5Response vendors) {
        for (TelematicsVendorInfo vendor : vendors.getData()) {
            System.out.println(vendor);
        }
    }
}
