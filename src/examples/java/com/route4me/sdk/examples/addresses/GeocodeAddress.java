package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.geocoding.GeocodingManager;
import com.route4me.sdk.services.routing.Geocodings;

/**
 * Geocodes (validates) one address before it is added to a route, with {@link GeocodingManager#geocode}, and prints
 * the matches, best first.
 */
public class GeocodeAddress {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        GeocodingManager manager = new GeocodingManager(apiKey);
        try {
            Geocodings[] matches = manager.geocode("1604 PARKRIDGE PKWY, Louisville, KY, 40214");
            if (matches != null) {
                for (Geocodings match : matches) {
                    System.out.println(match.getName() + " (" + match.getLatitude() + ", " + match.getLongitude()
                            + "), confidence " + match.getConfidence());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
