package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.tracking.TrackingManager;
import com.route4me.sdk.services.tracking.TrackingPing;
import com.route4me.sdk.services.tracking.TrackingPingsRequest;
import com.route4me.sdk.services.tracking.TrackingPingsResponse;
import java.util.Arrays;

/**
 * Records two GPS pings of a route in one request with {@link TrackingManager#setTrackingPings}. Set
 * {@code ROUTE_ID} and {@code MEMBER_ID} to a route of a test account and its driver: the example adds the pings to the
 * route's tracking history.
 */
public class SetTrackingPings {
    public static void main(String[] args) {
        String routeId = System.getenv("ROUTE_ID");
        String memberId = System.getenv("MEMBER_ID");
        if (routeId == null || routeId.isEmpty() || memberId == null || memberId.isEmpty()) {
            System.err.println("Set ROUTE_ID and MEMBER_ID to a route of a test account and its driver; this example"
                    + " records two GPS pings on the route.");
            return;
        }
        Long id = ExampleInput.toLong("MEMBER_ID", memberId);
        if (id == null) {
            return;
        }
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
        request.setMemberId(id);
        request.setDeviceGuid("EXAMPLE_DEVICE");
        request.setDeviceType("iphone");
        request.setRouteId(routeId);
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
