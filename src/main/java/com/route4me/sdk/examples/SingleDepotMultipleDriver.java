package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.model.Address;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.model.Parameters;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.enums.Constants.*;
import com.route4me.sdk.model.enums.Constants.DeviceType;
import com.route4me.sdk.model.enums.Constants.DistanceUnit;
import com.route4me.sdk.model.enums.Constants.Metric;
import com.route4me.sdk.model.enums.Constants.OptimizationState;
import com.route4me.sdk.model.enums.Constants.Optimize;
import com.route4me.sdk.model.enums.Constants.TravelMode;
import com.route4me.sdk.serdes.DataObjectDeserializer;
import com.route4me.sdk.serdes.DataObjectSerializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author juan
 */
public class SingleDepotMultipleDriver {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        OptimizationManager optimizationManager = route4me.getOptimizationManager();
        Map<String, String> params = new HashMap<>();
        optimizationManager.setParams(params);
        DataObject data = new DataObject();
        Parameters parameters = new Parameters();
        List<Address> addresses = new ArrayList<>();
        data.setParameters(parameters);
        data.setAddresses(addresses);
        parameters.setAlgorithm_type(AlgorithmType.CVRP_TW_SD.getValue());
        parameters.setStore_route(Boolean.FALSE);
        parameters.setShare_route(Boolean.FALSE);
        parameters.setRoute_time(0);
        parameters.setParts(20);
        parameters.setRoute_max_duration(86400);
        parameters.setVehicle_capacity("100");
        parameters.setVehicle_max_distance_mi("10000");
        parameters.setRoute_name("Single Depot, Multiple Driver");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistance_unit(DistanceUnit.MI.toString());
        parameters.setDevice_type(DeviceType.WEB.toString());
        parameters.setTravel_mode(TravelMode.DRIVING.toString());
        parameters.setMetric(Metric.ROUTE4ME_METRIC_GEODESIC.getValue());
         addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214",Boolean.TRUE, 38.141598, -85.793846, 300));
        addresses.add(new Address("1407 MCCOY, Louisville, KY, 40215",38.202496,-85.786514,300));
        addresses.add(new Address("4805 BELLEVUE AVE, Louisville, KY, 40215",38.178844,-85.774864,300));
        addresses.add(new Address("730 CECIL AVENUE, Louisville, KY, 40211",38.248684,-85.821121,300));
        addresses.add(new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211",38.251923,-85.800034,300));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216",38.176067,-85.824638,300));
        addresses.add(new Address("4738 BELLEVUE AVE, Louisville, KY, 40215",38.179806,-85.775558,300));
        addresses.add(new Address("318 SO. 39TH STREET, Louisville, KY, 40212",38.259335,-85.815094, 300));
        addresses.add(new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215",38.179253,-85.785118, 300));
        addresses.add(new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214",38.162472,-85.792854, 300));
        optimizationManager.setData(data);
        Response response = optimizationManager.runOptimization();
        DataObject responseObject = DataObjectDeserializer.GSON_DESERIALIZER.fromJson(response.getResponseBody(), DataObject.class);
        String jsonResponse = DataObjectSerializer.GSON_SERIALIZER.toJson(responseObject);
        System.out.println(jsonResponse);
        System.out.println("Response Code:" + response.getResponseCode());
        System.out.println("Optimization Problem ID:" + responseObject.getOptimization_problem_id());
        System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
        if (responseObject.getRoutes() != null) {
            if (responseObject.getRoutes() != null) {
                for (Routes route : responseObject.getRoutes()) {
                    System.out.println();
                    System.out.println("Route Name: " + route.getParameters().getRoute_name());
                    System.out.println("Route ID: " + route.getRoute_id());
                    if (route.getAddresses() != null) {
                        for (Address address : route.getAddresses()) {
                            System.out.println("Address:" + address.getAddress());
                        }
                    }
                }
            }
        }
    }
}
