package com.route4me.sdk.examples.tracking;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.managers.TrackingManager;
import com.route4me.sdk.services.tracking.TrackingHistory;
import com.route4me.sdk.model.enums.Constants.DeviceType;
import com.route4me.sdk.model.enums.Constants.Format;
import com.route4me.sdk.serdes.DataObjectDeserializer;
import com.route4me.sdk.serdes.DataObjectSerializer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author juan
 */
public class TrackDeviceLastLocationHistory {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        RouteManager route = route4me.getRouteManager();
        Map<String, String> params = new HashMap<>();
        params.put("format", Format.CSV.toString());
        params.put("route_id", "742A9E5051AA84B9E6365C92369B030C");
        params.put("lat", "33.14384");
        params.put("lng", "-83.22466");
        params.put("course", "1");
        params.put("speed", "120");
        params.put("device_type", DeviceType.IPHONE.toString());
        params.put("member_id", "1");
        params.put("device_guid", "TEST_GPS");
        params.put("device_timestamp", "2014-06-14 17:43:35");
        TrackingManager trackingManager = route4me.getTrackingManager();
        trackingManager.setParams(params);
        Response response = trackingManager.setGPSPosition();
        System.out.println(response.getResponseBody());
        params.clear();
        params.put("route_id", "742A9E5051AA84B9E6365C92369B030C");
        params.put("device_tracking_history", "1");
        route.setParams(params);
        response = trackingManager.getLastLocation();
        DataObject responseObject = DataObjectDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), DataObject.class);
        String jsonResponse = DataObjectSerializer.GSON_SERIALIZER.toJson(responseObject);
        System.out.println(jsonResponse);
        System.out.println("Response Code:" + response.getResponseCode());
        System.out.println("Optimization Problem ID:" + responseObject.getOptimization_problem_id());
        if (responseObject.getTracking_history() != null) {
            for (TrackingHistory th : responseObject.getTracking_history()) {
                System.out.println("Speed:" + th.getS());
                System.out.println("Longitude:" + th.getLg());
                System.out.println("Latitude:" + th.getLt());
                System.out.println("Time Stamp:" + th.getTs_friendly());
                System.out.println();
            }
        }

    }

}
