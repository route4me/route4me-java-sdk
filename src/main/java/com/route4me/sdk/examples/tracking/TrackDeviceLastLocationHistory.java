package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.tracking.TrackingManager;

public class TrackDeviceLastLocationHistory {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingManager manager = new TrackingManager(apiKey);
        try {
            DataObject obj = manager.getLastLocation("1402075EBA9A32939B2696DB1D7EE48E");
            System.out.println(obj.getTrackingHistory());
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
