package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.model.Base;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Route;
import com.route4me.sdk.model.SetGPS;
import com.route4me.sdk.model.TrackingHistory;
import com.route4me.sdk.model.enums.Constants.DeviceType;
import com.route4me.sdk.model.enums.Constants.Format;
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
        Route route = route4me.getRoute();
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
        SetGPS gps = route4me.getSetGPS();
        gps.setParams(params);
        Response response = route4me.setGPSPosition();
        System.out.println(response.getResponseBody());
        params.clear();
        params.put("route_id", "742A9E5051AA84B9E6365C92369B030C");
        params.put("device_tracking_history", "1");
        route.setParams(params);
        response = route4me.getLastLocation();
        DataObject responseObject = Base.GSONDeserializer.fromJson(response.getResponseBody(), DataObject.class);
        String jsonResponse = Base.GSONSerializer.toJson(responseObject);
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
