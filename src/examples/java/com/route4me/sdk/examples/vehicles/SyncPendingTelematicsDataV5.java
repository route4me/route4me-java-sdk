package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Syncs a pending vehicle from a telematics connection with {@link VehiclesManagerV5#syncPendingTelematicsData}.
 * Arguments: the telematics connection ID and the vehicle ID on that connection.
 */
public class SyncPendingTelematicsDataV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleV5 pending = new VehicleV5();
            pending.setVehicleAlias("Synced vehicle");
            pending.setR4mTelematicsGatewayConnectionId(args.length > 0 ? Long.parseLong(args[0]) : 1L);
            pending.setR4mTelematicsGatewayVehicleId(args.length > 1 ? Long.parseLong(args[1]) : 1L);
            VehicleV5 synced = manager.syncPendingTelematicsData(pending);
            System.out.println("Synced vehicle: " + synced.getVehicleId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
