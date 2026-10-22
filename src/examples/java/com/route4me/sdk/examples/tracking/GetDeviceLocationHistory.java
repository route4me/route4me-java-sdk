package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.tracking.DeviceLocationHistoryRequest;
import com.route4me.sdk.services.tracking.DeviceLocationHistoryResponse;
import com.route4me.sdk.services.tracking.TrackingHistory;
import com.route4me.sdk.services.tracking.TrackingManager;

/**
 * Prints the positions recorded on a route in the last two days with {@link TrackingManager#getDeviceLocationHistory}.
 * Set {@code ROUTE_ID} to the ID of one of your routes.
 */
public class GetDeviceLocationHistory {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingManager manager = new TrackingManager(apiKey);
        long now = System.currentTimeMillis() / 1000L;
        DeviceLocationHistoryRequest request = new DeviceLocationHistoryRequest()
                .setRouteId(System.getenv("ROUTE_ID"))
                .setTimePeriod("custom")
                .setStartDate(now - 2L * 24L * 60L * 60L)
                .setEndDate(now);
        try {
            DeviceLocationHistoryResponse history = manager.getDeviceLocationHistory(request);
            if (history.getData() != null) {
                for (TrackingHistory point : history.getData()) {
                    System.out.println(point.getFriendlyTimestamp() + ": " + point.getLatitude() + ", "
                            + point.getLongitude());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
