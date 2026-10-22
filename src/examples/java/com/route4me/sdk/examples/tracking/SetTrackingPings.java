package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.tracking.TrackingManager;
import com.route4me.sdk.services.tracking.TrackingPing;
import com.route4me.sdk.services.tracking.TrackingPingsRequest;
import com.route4me.sdk.services.tracking.TrackingPingsResponse;
import java.util.Arrays;

/**
 * Records two GPS pings of a route in one request with {@link TrackingManager#setTrackingPings}. Set
 * {@code ROUTE_ID} and {@code MEMBER_ID} to one of your routes and its driver.
 */
public class SetTrackingPings {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingManager manager = new TrackingManager(apiKey);
        long now = System.currentTimeMillis() / 1000L;
        TrackingPing first = new TrackingPing();
        first.setLat(33.14384);
        first.setLng(-83.22466);
        first.setDeviceTimestamp(now - 10L);
        TrackingPing second = new TrackingPing();
        second.setLat(33.14411);
        second.setLng(-83.22401);
        second.setDeviceTimestamp(now);
        TrackingPingsRequest request = new TrackingPingsRequest();
        request.setMemberId(Long.parseLong(System.getenv("MEMBER_ID")));
        request.setDeviceGuid("EXAMPLE_DEVICE");
        request.setDeviceType("iphone");
        request.setRouteId(System.getenv("ROUTE_ID"));
        request.setSpeedUnit("m/s");
        request.setItems(Arrays.asList(first, second));
        try {
            TrackingPingsResponse response = manager.setTrackingPings(request);
            System.out.println("Stored: " + response.getStatus() + ", rejected: " + response.getItemsErrors());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
