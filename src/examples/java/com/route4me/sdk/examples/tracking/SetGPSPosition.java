package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.tracking.GPSPosition;
import com.route4me.sdk.services.tracking.SetGpsResponse;
import com.route4me.sdk.services.tracking.TrackingManager;

/**
 * Records a GPS position of a route with {@link TrackingManager#setGPS}. Set {@code ROUTE_ID} and {@code MEMBER_ID}
 * to a route of a test account and its driver: the example adds a position to the route's tracking history.
 */
public class SetGPSPosition {

    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String memberId = System.getenv("MEMBER_ID");
        if (routeId == null || routeId.isEmpty() || memberId == null || memberId.isEmpty()) {
            System.err.println("Set ROUTE_ID and MEMBER_ID to a route of a test account and its driver; "
                    + "this example records a GPS position on it.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingManager manager = new TrackingManager(apiKey);
        GPSPosition position = new GPSPosition().setLatitude(33.14384).setLongitude(-83.22466).setAltitude(10D);
        position.setFormat(Constants.Format.CSV);
        position.setRouteId(routeId);
        position.setCourse(1);
        position.setSpeed(120F);
        position.setDeviceType(Constants.DeviceType.IPAD)
                .setDeviceGUID("TEST_GPS")
                .setDeviceTimestamp("2014-06-14 17:43:35");
        position.setMemberId(memberId);
        try {
            SetGpsResponse response = manager.setGPS(position);
            if (response == null) {
                System.out.println("The server sent an empty answer");
            } else if (response.getAnswerText() != null) {
                System.out.println("Answer: " + response.getAnswerText());
            } else {
                System.out.println("Stored: " + response.getStatus() + ", transaction: " + response.getTxId());
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }

    }

}
