package com.route4me.sdk.examples.routes;

import com.route4me.sdk.examples.*;
import com.route4me.sdk.Route4Me;
import com.route4me.sdk.model.Address;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.model.Parameters;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.enums.Constants.*;
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
public class DeleteRoute {

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
        parameters.setAlgorithm_type(AlgorithmType.TSP.getValue());
        parameters.setStore_route(Boolean.FALSE);
        parameters.setShare_route(Boolean.FALSE);
        parameters.setRoute_name("Single Driver Route 10 Stops");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistance_unit(DistanceUnit.MI.toString());
        parameters.setDevice_type(DeviceType.WEB.toString());
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214",Boolean.TRUE, 38.141598, -85.793846, 300, 29400, 30000));
        addresses.add(new Address("1407 MCCOY, Louisville, KY, 40215",38.202496,-85.786514,300,30000,30600));
        addresses.add(new Address("4805 BELLEVUE AVE, Louisville, KY, 40215",38.178844,-85.774864,300,30600,31200));
        addresses.add(new Address("730 CECIL AVENUE, Louisville, KY, 40211",38.248684,-85.821121,300,31200,31800));
        addresses.add(new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211",38.251923,-85.800034,300,31800,32400));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216",38.176067,-85.824638,300,32400,33000));
        addresses.add(new Address("4738 BELLEVUE AVE, Louisville, KY, 40215",38.179806,-85.775558,300,33000,33600));
        addresses.add(new Address("318 SO. 39TH STREET, Louisville, KY, 40212",38.259335,-85.815094,300,33600,34200));
        addresses.add(new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215",38.179253,-85.785118,300,34200,34800));
        addresses.add(new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214",38.162472,-85.792854,300,34800,35400));
        addresses.add(new Address("1661 W HILL ST, Louisville, KY, 40210",38.229584,-85.783966,300,35400,36000));        
        optimizationManager.setData(data);
        DataObject responseObject = optimizationManager.runOptimization();
        String routeID = responseObject.getRoutes().get(0).getRoute_id();
        RouteManager routeManager = route4me.getRouteManager();
        Routes response = routeManager.deleteRoute(routeID);
        System.out.println("Route ID: " + response.getRoute_id());
        System.out.println("Deleted: " + response.getDeleted());
    }

}
