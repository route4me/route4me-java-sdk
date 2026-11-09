package com.route4me.sdk.examples.vehicles;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.vehicles.VehiclesManagerV5;

/**
 * Syncs a pending vehicle from a telematics connection with {@link VehiclesManagerV5#syncPendingTelematicsData}.
 * Arguments: the telematics connection ID and the vehicle ID on that connection; without them, the example reads
 * {@code TELEMATICS_CONNECTION_ID} and {@code TELEMATICS_VEHICLE_ID}. Use a connection of a test account.
 */
public class SyncPendingTelematicsDataV5 {
    public static void main(String[] args) {
        String connectionId = args.length > 0 ? args[0] : System.getenv("TELEMATICS_CONNECTION_ID");
        String telematicsVehicleId = args.length > 1 ? args[1] : System.getenv("TELEMATICS_VEHICLE_ID");
        if (connectionId == null || connectionId.isEmpty() || telematicsVehicleId == null
                || telematicsVehicleId.isEmpty()) {
            System.err.println("Pass the telematics connection ID and the vehicle ID on that connection as arguments,"
                    + " or set TELEMATICS_CONNECTION_ID and TELEMATICS_VEHICLE_ID to a connection of a test account"
                    + " and a vehicle on it; this example syncs the vehicle into the account.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        VehiclesManagerV5 manager = new VehiclesManagerV5(apiKey);
        try {
            VehicleV5 pending = new VehicleV5();
            pending.setVehicleAlias("Synced vehicle");
            pending.setR4mTelematicsGatewayConnectionId(Long.parseLong(connectionId));
            pending.setR4mTelematicsGatewayVehicleId(Long.parseLong(telematicsVehicleId));
            VehicleV5 synced = manager.syncPendingTelematicsData(pending);
            System.out.println("Synced vehicle: " + synced.getVehicleId());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
