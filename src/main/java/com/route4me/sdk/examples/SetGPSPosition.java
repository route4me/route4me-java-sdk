package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.SetGPS;
import java.util.HashMap;
import java.util.Map;
import com.route4me.sdk.model.enums.Constants.*;

/**
 *
 * @author juan
 */
public class SetGPSPosition {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
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

    }

}
