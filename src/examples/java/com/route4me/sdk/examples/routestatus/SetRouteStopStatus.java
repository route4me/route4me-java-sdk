package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.StatusResponse;

/**
 * Sets the status of route stops with {@link RouteStatusManager#setRouteStopStatus}. Set
 * {@code ROUTE_DESTINATION_IDS} to the comma-separated IDs of stops of a test account, and {@code STOP_STATUS} to the
 * status to give them, such as {@code completed}: the example changes them.
 */
public class SetRouteStopStatus {
    public static void main(String[] args) {
        String ids = System.getenv("ROUTE_DESTINATION_IDS");
        String status = System.getenv("STOP_STATUS");
        if (ids == null || ids.isEmpty() || status == null || status.isEmpty()) {
            System.err.println("Set ROUTE_DESTINATION_IDS to the comma-separated IDs of stops of a test account, and"
                    + " STOP_STATUS to their new status; this example changes them.");
            return;
        }
        long[] destinationIds;
        try {
            String[] parts = ids.split(",");
            destinationIds = new long[parts.length];
            for (int i = 0; i < parts.length; i++) {
                destinationIds[i] = Long.parseLong(parts[i].trim());
            }
        } catch (NumberFormatException e) {
            System.err.println("ROUTE_DESTINATION_IDS must be comma-separated numbers: " + ids);
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RouteStatusManager manager = new RouteStatusManager(apiKey);
        try {
            StatusResponse response = manager.setRouteStopStatus(destinationIds, status);
            System.out.println("Status set: " + response.getStatus());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
