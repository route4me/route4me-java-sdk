package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.tracking.FindAssetResponse;
import com.route4me.sdk.services.tracking.TrackingManager;

/**
 * Prints where an order is and its status history, by its tracking number, with {@link TrackingManager#findAsset}.
 * Pass the tracking number as the first argument.
 */
public class FindAsset {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        TrackingManager manager = new TrackingManager(apiKey);
        try {
            FindAssetResponse asset = manager.findAsset(args[0]);
            System.out.println("Destination: " + asset.getDestinationAddress1() + ", delivered: "
                    + asset.getDelivered());
            if (asset.getStatusHistory() != null) {
                for (FindAssetResponse.StatusHistory status : asset.getStatusHistory()) {
                    System.out.println(status.getUnixTimestamp() + ": " + status.getInfo());
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
