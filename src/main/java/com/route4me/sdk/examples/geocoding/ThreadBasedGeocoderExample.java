/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.geocoding;

import com.route4me.sdk.services.geocoding.GeocoderOptions;
import com.route4me.sdk.services.geocoding.GeocoderOptions.GeocoderDetails;
import com.route4me.sdk.services.geocoding.GeocodingManager;
import com.route4me.sdk.services.routing.Address;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadBasedGeocoderExample {

    private static final Logger logger = LogManager.getLogger(ThreadBasedGeocoderExample.class);

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";

        GeocodingManager manager = new GeocodingManager(apiKey);

        List<String> addresses = Arrays.asList(
                                "2271 SCANLAN ST., LONDON, N5W 6G9",
                                "112 COMMISSIONERS RD., EMBRO, N4V 1V9",
                                "82 HOPE ST. W., TAVISTOCK, N0B 2R0",
                                "70 HOPE ST. W., TAVISTOCK, N0B 2R0",
                                "485 WOODLAWN RD. W., GUELPH, N1H 7P7",
                                "183 SILVERCREEK PKWY N, GUELPH, N1H 3T2	",
                                "183 SILVERCREEK PARKWAY N., GUELPH, N4G 3S9",
                                "183 SILVERCREEK PARKWAY N., GUELPH, N1H 3T2",
                                "130 SILVERCREEK PKY. N., GUELPH, N1H 7Y5",
                                "104 SILVERCREEK PKWY N, GUELPH, N1H 7B4",
                                "650 SCOTTSDALE DR., GUELPH, N1G 3M2",
                                "425 STONE RD. W., GUELPH, N1G 2X6",
                                "585 SCOTTSDALE DR., GUELPH, N1H 6H9",
                                "435 STONE RD W, GUELPH, N1G 2X6",
                                "372 STONE RD. WEST, GUELPH, N1G 2X6",
                                "372 STONE RD. WEST, GUELPH, N1G 2X6",
                                "175 STONE RD. W., GUELPH, N1G 2X6",
                                "80 STONE RD. W., GUELPH, N1G 5L4",
                                "7 CLAIR RD W, GUELPH, N1L 0A6",
                                "101 CLAIR RD E, GUELPH, N1L 0J7",
                                "65 GORDON ST., GUELPH, N1G 4Z1",
                                "65 GORDON ST., GUELPH, N1G 4Z1",
                                "243 WOODLAWN AVE. W., GUELPH, N1H 1G8",
                                "243 WOODLAWN AVE. W., GUELPH, N1H 1G8",
                                "11 WOODLAWN RD. WEST, GUELPH, N1E 2N4",
                                "49 WOODLAWN RD. W., GUELPH, N1E 1N5",
                                "40 HIGH ST., ELORA, N1H 1G8",
                                "7445 WELLINGTON COUNTY RD. #21, ELORA, V0B 1S0",
                                "875 ST DAVID ST N, FERGUS, N1M 2W3",
                                "900 TOWER ST S, FERGUS, N1M 3N7",
                                "870 TOWER ST. S., FERGUS, N0B 1S0",
                                "870 TOWER ST. S., FERGUS, N1M 2P6",
                                "710 TOWER ST. S., FERGUS, N1M 2R3",
                                "201 ANDREW ST. W., FERGUS, N1M 2P6",
                                "97 PARKSIDE PLACE, FERGUS, N1M 2W8",
                                "801 ST. DAVID ST. N., FERGUS, N1M 3M5",
                                "165 GEORGE ST., ARTHUR, N0G 1A0",
                                "8006 WELLINGTON RD., ARTHUR, N0G 1A0",
                                "5201 AMENT LINE, LINWOOD, N0B 2A0",
                                "5 WOODSTOCK ST. N., TAVISTOCK, N0B 2R0",
                                "23 WOODSTOCK ST. S., TAVISTOCK, N3T 1M2",
                                "595771 HWY #59, HUNTINGFORD, N4S 7W1",
                                "2271 SCANLAN ST., LONDON, N5W 6G9",
                                ",",
                                ".",
                                "1",
                                ",.#",
                                "@.#",
                                "THIS IS AN INVALID ADDRESS");

        GeocoderOptions options = new GeocoderOptions();

        options.setDetailed(GeocoderDetails.DETAILED);
        options.setMaxThreads(40);
        options.setMaxRetries(10);
        options.setMaxTimeout(500l);

        logger.debug("Running Geocoder with " + options.getMaxThreads() + " Threads.");
        long startTime = System.currentTimeMillis();

        List<Address> geocodedAddresses = manager.bulkGeocoder(addresses, options);

        logger.debug("Processed " + addresses.size() + " - Elapsed Time (seconds): " + (System.currentTimeMillis() - startTime) / 1000f);
        logger.debug("All Threads Finished ");

        logger.debug("Addresses Geocoded: " + geocodedAddresses.size());

        int i = 0;
        for (Address geocodedAddress : geocodedAddresses) {
            i++;
            logger.debug("ADDRESS: (" + i + ")\t" + geocodedAddress.getAddress() + "\t" + geocodedAddress.getLatitude() + "\t" + geocodedAddress.getLongitude() + "\tGEOCODINGS:\t" + geocodedAddress.getGeocodings().get(0).getType() + "\t" + geocodedAddress.getGeocodings().get(0).getLatitude() + "\t" + geocodedAddress.getGeocodings().get(0).getLongitude());

        }

    }

}
