package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.managers.AddressesManager;
import com.route4me.sdk.model.Address;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.model.Parameters;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.enums.Constants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author juan
 */
public class DeleteAddress {

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
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", Boolean.TRUE, 38.141598, -85.793846, 300, 29400, 30000));
        addresses.add(new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300, 30000, 30600));
        addresses.add(new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864, 300, 30600, 31200));
        addresses.add(new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300, 31200, 31800));
        addresses.add(new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300, 31800, 32400));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300, 32400, 33000));
        optimizationManager.setData(data);
        DataObject responseObject = optimizationManager.runOptimization();
        System.out.println("Addresses in the current Optimization");
        if (responseObject.getAddresses() != null) {
            for (Address address : responseObject.getAddresses()) {
                System.out.println("Address:" + address.getAddress());
                System.out.println("Address ID:" + address.getRoute_destination_id());
                System.out.println("Route ID:" + address.getRoute_id());
                System.out.println("Sequence No:" + address.getSequence_no());
                System.out.println("Is Depot:" + address.isIs_depot());
            }
        }
        AddressesManager addressesManager = route4me.getAddressesManager();
        String routeID = responseObject.getRoutes().get(0).getRoute_id();
        Number routeDestinationID = responseObject.getRoutes().get(0).getAddresses().get(0).getRoute_destination_id();
        Response response = addressesManager.deleteAddress(routeID, routeDestinationID);
        System.out.println(response.getResponseBody());
    }

}
