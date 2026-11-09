package com.route4me.sdk.examples.routestatus;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routestatus.RouteStatusManager;
import com.route4me.sdk.services.routestatus.StatusResponse;
import java.util.List;

/**
 * Sets the status of route stops with {@link RouteStatusManager#setRouteStopStatus}. Set
 * {@code ROUTE_DESTINATION_IDS} to the comma-separated IDs of stops of a test account, and {@code STOP_STATUS} to the
 * status to give them, such as {@code completed}: the example changes them.
 */
public class SetRouteStopStatus {
    public static void main(String[] args) {
        List<Long> ids = ExampleInput.toLongs("ROUTE_DESTINATION_IDS", System.getenv("ROUTE_DESTINATION_IDS"));
        if (ids == null) {
            return;
        }
        String status = System.getenv("STOP_STATUS");
        if (ids.isEmpty() || status == null || status.isEmpty()) {
            System.err.println("Set ROUTE_DESTINATION_IDS to the comma-separated IDs of stops of a test account, and"
                    + " STOP_STATUS to their new status; this example changes them.");
            return;
        }
        long[] destinationIds = new long[ids.size()];
        for (int i = 0; i < destinationIds.length; i++) {
            destinationIds[i] = ids.get(i);
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
