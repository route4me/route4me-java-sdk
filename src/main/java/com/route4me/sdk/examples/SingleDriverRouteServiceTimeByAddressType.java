// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants.*;
import com.route4me.sdk.services.routing.*;
import static com.route4me.sdk.utils.ServiceTimeUtils.getServiceTimeByAddressType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author route4me
 */
public class SingleDriverRouteServiceTimeByAddressType {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        List<Address> addresses = new ArrayList<>();
        parameters.setAlgorithmType(AlgorithmType.TSP.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteName("Single Driver Route 10 Stops");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        optParameters.setParameters(parameters);

        Map<String, Integer> serviceTimeByAddressTypeMap = Stream.of(new Object[][]{
            {"STOP_TYPE_1", 60},
            {"STOP_TYPE_2", 120},
            {"STOP_TYPE_3", 180},
            {"STOP_TYPE_4", 300},}).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

        //DEPOT
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 0));

        //STOPS
        Address address = new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_1", serviceTimeByAddressTypeMap));
        addresses.add(address);

        address = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_1", serviceTimeByAddressTypeMap));
        addresses.add(address);

        address = new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_2", serviceTimeByAddressTypeMap));
        addresses.add(address);

        address = new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_2", serviceTimeByAddressTypeMap));
        addresses.add(address);

        address = new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_3", serviceTimeByAddressTypeMap));
        addresses.add(address);

        address = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_3", serviceTimeByAddressTypeMap));
        addresses.add(address);

        address = new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_4", serviceTimeByAddressTypeMap));
        addresses.add(address);

        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_4", serviceTimeByAddressTypeMap));
        addresses.add(address);

        address = new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214", 38.162472, -85.792854);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_4", serviceTimeByAddressTypeMap));
        addresses.add(address);

        address = new Address("1661 W HILL ST, Louisville, KY, 40210", 38.229584, -85.783966);
        address.setTime(getServiceTimeByAddressType("STOP_TYPE_5", serviceTimeByAddressTypeMap));
        addresses.add(address);

        optParameters.setAddresses(addresses);

        try {
            DataObject responseObject = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
            System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
            if (responseObject.getAddresses() != null) {
                for (Address sequencedAddress : responseObject.getAddresses()) {
                    System.out.println(sequencedAddress.getSequenceNo() + " - " + sequencedAddress.getAddress() + "  Service Time: " + sequencedAddress.getTime());
                }
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}
// codebeat:enable[SIMILARITY]
