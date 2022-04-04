// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.pickupdropoff;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants.*;
import com.route4me.sdk.services.routing.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author juan
 */
public class PickupDropoffJoint {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.CVRP_TW_SD.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime(28800);
        parameters.setVehicleCapacity("100");
        parameters.setRouteName("Pickup Dropoff Joint Example");
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());
        optParameters.setParameters(parameters);

        List<Address> addresses = new ArrayList<>();
        // Depot
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 300));
        
        // Stops
        Address address = new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300);
        address.setPickUp("PD0001");
        address.setAlias("Pickup - Customer 001");
        addresses.add(address);

        address = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864, 300);
        address.setPickUp("PD0004");
        address.setJoint(1);
        address.setAlias("Pickup - Customer 004");
        addresses.add(address);

        address = new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300);
        address.setDropOff("PD0003");
        address.setAlias("Dropoff - Customer 003");
        addresses.add(address);

        address = new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300);
        addresses.add(address);

        address = new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300);
        address.setPickUp("PD0002");
        address.setAlias("Pickup - Customer 002");
        addresses.add(address);

        address = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300);
        address.setPickUp("PD0003");
        address.setAlias("Pickup - Customer 003");
        addresses.add(address);

        address = new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300);
        address.setDropOff("PD0004");
        address.setAlias("Dropoff - Customer 004");
        addresses.add(address);

        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300);
        address.setDropOff("PD0002");
        address.setAlias("Dropoff - Customer 002");
        addresses.add(address);

        address = new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214", 38.162472, -85.792854, 300);
        address.setDropOff("PD0001");
        address.setAlias("Dropoff - Customer 001");
        addresses.add(address);
        optParameters.setAddresses(addresses);

        try {
            DataObject responseObject = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
            System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
            if (responseObject.getAddresses() != null) {
                for (Iterator<Address> it = responseObject.getAddresses().iterator(); it.hasNext();) {
                    address = it.next();
                    System.out.println(address);
                }
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
// codebeat:enable[SIMILARITY]
