/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.geocoding;

import com.route4me.sdk.services.geocoding.GeocoderWebSockets;
import com.route4me.sdk.services.geocoding.GeocodingManager;
import com.route4me.sdk.services.routing.Address;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebSocketsGeocoderExample {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        GeocodingManager geocodingManager = new GeocodingManager(apiKey);
        
        List<Address> addresses = new ArrayList<>();
        

        addresses.add(new Address("2271 SCANLAN ST., LONDON, N5W 6G9"));
        addresses.add(new Address("112 COMMISSIONERS RD., EMBRO, N4V 1V9"));
        addresses.add(new Address("82 HOPE ST. W., TAVISTOCK, N0B 2R0"));
        addresses.add(new Address("70 HOPE ST. W., TAVISTOCK, N0B 2R0"));
        addresses.add(new Address("485 WOODLAWN RD. W., GUELPH, N1H 7P7"));
        addresses.add(new Address("183 SILVERCREEK PKWY N, GUELPH, N1H 3T2	"));
        addresses.add(new Address("183 SILVERCREEK PARKWAY N., GUELPH, N4G 3S9"));
        addresses.add(new Address("183 SILVERCREEK PARKWAY N., GUELPH, N1H 3T2"));
        addresses.add(new Address("130 SILVERCREEK PKY. N., GUELPH, N1H 7Y5"));
        addresses.add(new Address("104 SILVERCREEK PKWY N, GUELPH, N1H 7B4"));
        addresses.add(new Address("650 SCOTTSDALE DR., GUELPH, N1G 3M2"));
        addresses.add(new Address("425 STONE RD. W., GUELPH, N1G 2X6"));
        addresses.add(new Address("585 SCOTTSDALE DR., GUELPH, N1H 6H9"));
        addresses.add(new Address("435 STONE RD W, GUELPH, N1G 2X6"));
        addresses.add(new Address("435 STONE RD W, GUELPH, N1G 2X6"));
        addresses.add(new Address("435 STONE RD W, GUELPH, N1G 2X6"));
        addresses.add(new Address("435 STONE RD W, GUELPH, N1G 2X6"));
        addresses.add(new Address("435 STONE RD W, GUELPH, N1G 2X6"));
        addresses.add(new Address("435 STONE RD W, GUELPH, N1G 2X6"));
        addresses.add(new Address("435 STONE RD W, GUELPH, N1G 2X6"));
        addresses.add(new Address("372 STONE RD. WEST, GUELPH, N1G 2X6"));
        addresses.add(new Address("372 STONE RD. WEST, GUELPH, N1G 2X6"));
        addresses.add(new Address("175 STONE RD. W., GUELPH, N1G 2X6"));
        addresses.add(new Address("80 STONE RD. W., GUELPH, N1G 5L4"));
        addresses.add(new Address("7 CLAIR RD W, GUELPH, N1L 0A6"));
        addresses.add(new Address("101 CLAIR RD E, GUELPH, N1L 0J7"));
        addresses.add(new Address("65 GORDON ST., GUELPH, N1G 4Z1"));
        addresses.add(new Address("65 GORDON ST., GUELPH, N1G 4Z1"));
        addresses.add(new Address("243 WOODLAWN AVE. W., GUELPH, N1H 1G8"));
        addresses.add(new Address("243 WOODLAWN AVE. W., GUELPH, N1H 1G8"));
        addresses.add(new Address("11 WOODLAWN RD. WEST, GUELPH, N1E 2N4"));
        addresses.add(new Address("49 WOODLAWN RD. W., GUELPH, N1E 1N5"));
        addresses.add(new Address("40 HIGH ST., ELORA, N1H 1G8"));
        addresses.add(new Address("7445 WELLINGTON COUNTY RD. #21, ELORA, V0B 1S0"));
        addresses.add(new Address("875 ST DAVID ST N, FERGUS, N1M 2W3"));
        addresses.add(new Address("900 TOWER ST S, FERGUS, N1M 3N7"));
        addresses.add(new Address("870 TOWER ST. S., FERGUS, N0B 1S0"));
        addresses.add(new Address("870 TOWER ST. S., FERGUS, N1M 2P6"));
        addresses.add(new Address("710 TOWER ST. S., FERGUS, N1M 2R3"));
        addresses.add(new Address("201 ANDREW ST. W., FERGUS, N1M 2P6"));
        addresses.add(new Address("97 PARKSIDE PLACE, FERGUS, N1M 2W8"));
        addresses.add(new Address("801 ST. DAVID ST. N., FERGUS, N1M 3M5"));
        addresses.add(new Address("165 GEORGE ST., ARTHUR, N0G 1A0"));
        addresses.add(new Address("8006 WELLINGTON RD., ARTHUR, N0G 1A0"));
        addresses.add(new Address("5201 AMENT LINE, LINWOOD, N0B 2A0"));
        addresses.add(new Address("5 WOODSTOCK ST. N., TAVISTOCK, N0B 2R0"));
        addresses.add(new Address("23 WOODSTOCK ST. S., TAVISTOCK, N3T 1M2"));
        addresses.add(new Address("595771 HWY #59, HUNTINGFORD, N4S 7W1"));
        addresses.add(new Address("2271 SCANLAN ST., LONDON, N5W 6G9"));
        
        GeocoderWebSockets job = geocodingManager.websocketsGeocoder(addresses);
        
        
        while (!job.isCompleted()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GeocoderWebSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        addresses = job.getAddresses();
        System.out.println(addresses);
        
    }


    
}
