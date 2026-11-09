package com.route4me.sdk.examples.geocoding;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.geocoding.GeocodingManager;

/**
 * Geocodes an address, then reverse geocodes a point, with {@link GeocodingManager#geocodeAddresses}.
 */
public class GeocodeAddresses {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        GeocodingManager manager = new GeocodingManager(apiKey);
        try {
            System.out.println(manager.geocodeAddresses("Los Angeles International Airport, CA", "json"));
            System.out.println(manager.geocodeAddresses("33.945705,-118.391105", "json"));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
