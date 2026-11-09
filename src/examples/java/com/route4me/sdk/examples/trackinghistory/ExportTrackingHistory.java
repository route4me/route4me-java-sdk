package com.route4me.sdk.examples.trackinghistory;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.trackinghistory.TrackingHistoryExportRequest;
import com.route4me.sdk.services.trackinghistory.TrackingHistoryManager;

/**
 * Exports the tracking history of a member for the last seven days as CSV with
 * {@link TrackingHistoryManager#exportTrackingHistory}. Set {@code MEMBER_ID} to one of your members.
 */
public class ExportTrackingHistory {
    public static void main(String[] args) {
        String memberId = System.getenv("MEMBER_ID");
        if (memberId == null || memberId.isEmpty()) {
            System.err.println("Set MEMBER_ID to the ID of one of your members; this example exports their tracking"
                    + " history.");
            return;
        }
        Long id = ExampleInput.toLong("MEMBER_ID", memberId);
        if (id == null) {
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingHistoryManager manager = new TrackingHistoryManager(apiKey);
        TrackingHistoryExportRequest request = new TrackingHistoryExportRequest()
                .setMemberId(id)
                .setTimePeriod("7days")
                .setFormat("csv");
        try {
            System.out.println(manager.exportTrackingHistory(request));
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
