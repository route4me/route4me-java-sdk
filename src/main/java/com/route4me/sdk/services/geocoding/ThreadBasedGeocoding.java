/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.GeoCoordinates;
import com.route4me.sdk.services.routing.Geocodings;
import java.util.Arrays;
import java.util.concurrent.Callable;
import org.apache.http.client.utils.URIBuilder;


public class ThreadBasedGeocoding implements Callable {
    
    private static final String GEOCODING_ADDRESS = "/api/address.php";

    
    private final String address;
    private final GeocodingManager manager;
    private final GeocoderOptions options;

    public ThreadBasedGeocoding(GeocodingManager manager, String address, GeocoderOptions options) {
        this.manager = manager;
        this.address = address;
        this.options = options;
    }


    protected static URIBuilder defaultBuilder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("api.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }
    
    private URIBuilder getBuilder(){
        URIBuilder builder = defaultBuilder(GEOCODING_ADDRESS);
        builder.setParameter("address", address);
        builder.setParameter("format", options.getResponseFormat().getValue());
        builder.setParameter("detailed", options.getDetailed().getValue());
        return builder;
    }


    private <T> T geocoder(Class<T> clazz) {
        return  manager.forwardGeocode(getBuilder(), clazz);
    }

    @Override
    public String toString() {
        return this.address;
    }

    @Override
    public Address call() throws Exception {
        Address geocodedAddress = new Address();
        geocodedAddress.setAddress(address);

        switch(options.getDetailed()) {
            case DETAILED:
                Geocodings[] geocodes = geocoder(Geocodings[].class);
                if (geocodes != null && options.getDetailed().equals(GeocoderOptions.GeocoderDetails.DETAILED)){
                    geocodedAddress.setGeocodings(Arrays.asList(geocodes));
                    geocodedAddress.setLatitude(geocodes[0].getCoordinates().getLatitude());
                    geocodedAddress.setLongitude(geocodes[0].getCoordinates().getLongitude());
                break;
        }
            case SIMPLE:
                GeoCoordinates geocoordinate = geocoder(GeoCoordinates.class);
                if (geocoordinate != null){
                    geocodedAddress.setLatitude(geocoordinate.getLatitude());
                    geocodedAddress.setLongitude(geocoordinate.getLongitude());
                }
                break;
        }
        
        return geocodedAddress;
    }

}
