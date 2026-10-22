package com.route4me.sdk.examples.geocoding;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.geocoding.GeocodingManager;

/** Geocodes two addresses in one request, as JSON, with {@link GeocodingManager#batchGeocoding}. */
public class BatchGeocoding {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        GeocodingManager manager = new GeocodingManager(apiKey);
        String addresses = "Los Angeles International Airport, CA||3495 Purdue St, Cuyahoga Falls, OH 44221";
        try {
            System.out.println(manager.batchGeocoding(addresses, "json"));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
