package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.tracking.GPSPosition;
import com.route4me.sdk.services.tracking.TrackingManager;

/**
 * @author juan
 */
public class SetGPSPosition {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        TrackingManager manager = new TrackingManager(apiKey);
        GPSPosition position = new GPSPosition().setLatitude(33.14384).setLongitude(-83.22466).setAltitude(10D);
        position.setFormat(Constants.Format.CSV);
        position.setRouteId("1402075EBA9A32939B2696DB1D7EE48E");
        position.setCourse(1);
        position.setSpeed(120F);
        position.setDeviceType(Constants.DeviceType.IPAD)
                .setDeviceGUID("TEST_GPS")
                .setDeviceTimestamp("2014-06-14 17:43:35");
        position.setMemberId("1");
        try {
            System.out.println(manager.setGPS(position));
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }

    }

}
