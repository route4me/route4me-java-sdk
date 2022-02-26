// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.pickupdropoff;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Constants.*;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author route4me
 */
public class PickupDropoffPriority {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey, false);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.CVRP_TW_SD.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime((8 + 5) * 3600); // 8:00 am East Time
        parameters.setParts(99);
        parameters.setLeftTurn(LeftTurn.LEFTTURN_FORBID.getValue());
        parameters.setUTurn(UTurn.UTURN_DEPART_TO_RIGHT.getValue());
        parameters.setVehicleCapacity("4");
        parameters.isUseMixedPickupDeliveryDemands(false);
        parameters.setRouteName("Pickup Dropoff Example");
        parameters.setOptimize(Optimize.TIME.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());
        optParameters.setParameters(parameters);

        List<Address> addresses = new ArrayList<>();
        // Depot
        Address depotAddress = new Address("455 S 4th St, Louisville, KY 40202", true, "DEPOT", 38.251698, -85.757308, 0);
        addresses.add(depotAddress);
        
        // Pickups
        
        Address pickupAddress;
        
        pickupAddress = new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", "STORAGE FACILITY", 38.141598, -85.793846, 840 * 7); 
        pickupAddress.setPickUp("PDA001");
        pickupAddress.setAddressStopType(AddressStopType.PICKUP.toString());
        pickupAddress.setPriority(1);
        pickupAddress.setPieces(1);
        pickupAddress.addCustomField("GOOD_TYPE", "TYPE0001");
        addresses.add(pickupAddress);
        pickupAddress = new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", "STORAGE FACILITY", 38.141598, -85.793846, 0); 
        pickupAddress.setPickUp("PDA002");
        pickupAddress.setAddressStopType(AddressStopType.PICKUP.toString());
        pickupAddress.setPriority(1);
        pickupAddress.setPieces(1);
        pickupAddress.addCustomField("GOOD_TYPE", "TYPE0002");
        addresses.add(pickupAddress);
        pickupAddress = new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", "STORAGE FACILITY", 38.141598, -85.793846, 0); 
        pickupAddress.setPickUp("PDA003");
        pickupAddress.setAddressStopType(AddressStopType.PICKUP.toString());
        pickupAddress.setPriority(1);
        pickupAddress.setPieces(1);
        pickupAddress.addCustomField("GOOD_TYPE", "TYPE0003");
        addresses.add(pickupAddress);
        pickupAddress = new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", "STORAGE FACILITY", 38.141598, -85.793846, 0); 
        pickupAddress.setPickUp("PDA004");
        pickupAddress.setAddressStopType(AddressStopType.PICKUP.toString());
        pickupAddress.setPriority(1);
        pickupAddress.setPieces(1);
        pickupAddress.addCustomField("GOOD_TYPE", "TYPE0004");
        addresses.add(pickupAddress);
        pickupAddress = new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", "STORAGE FACILITY", 38.141598, -85.793846, 0); 
        pickupAddress.setPickUp("PDA005");
        pickupAddress.setAddressStopType(AddressStopType.PICKUP.toString());
        pickupAddress.setPriority(1);
        pickupAddress.setPieces(1);
        pickupAddress.addCustomField("GOOD_TYPE", "TYPE0005");
        addresses.add(pickupAddress);
        pickupAddress = new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", "STORAGE FACILITY", 38.141598, -85.793846, 0); 
        pickupAddress.setPickUp("PDA006");
        pickupAddress.setAddressStopType(AddressStopType.PICKUP.toString());
        pickupAddress.setPriority(1);
        pickupAddress.setPieces(1);
        pickupAddress.addCustomField("GOOD_TYPE", "TYPE0006");
        addresses.add(pickupAddress);
        pickupAddress = new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", "STORAGE FACILITY", 38.141598, -85.793846, 0); 
        pickupAddress.setPickUp("PDA007");
        pickupAddress.setAddressStopType(AddressStopType.PICKUP.toString());
        pickupAddress.setPriority(1);
        pickupAddress.setPieces(1);
        pickupAddress.addCustomField("GOOD_TYPE", "TYPE0007");
        addresses.add(pickupAddress);
        

        //Dropoffs
        Address dropoffAddress;
        dropoffAddress = new Address("1407 MCCOY, Louisville, KY, 40215", "Customer 001", 38.202496, -85.786514, 840);
        dropoffAddress.setDropOff("PDA001");
        dropoffAddress.setAddressStopType(AddressStopType.DELIVERY.toString());
        dropoffAddress.setPieces(1);
        dropoffAddress.addCustomField("GOOD_TYPE", "TYPE0001");
        addresses.add(dropoffAddress);
        dropoffAddress = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", "Customer 002", 38.178844, -85.774864, 840);
        dropoffAddress.setDropOff("PDA002");
        dropoffAddress.setAddressStopType(AddressStopType.DELIVERY.toString());
        dropoffAddress.setPieces(1);
        dropoffAddress.addCustomField("GOOD_TYPE", "TYPE0002");
        addresses.add(dropoffAddress);
        dropoffAddress = new Address("730 CECIL AVENUE, Louisville, KY, 40211", "Customer 003", 38.248684, -85.821121, 840);
        dropoffAddress.setDropOff("PDA003");
        dropoffAddress.setAddressStopType(AddressStopType.DELIVERY.toString());
        dropoffAddress.setPieces(1);
        dropoffAddress.addCustomField("GOOD_TYPE", "TYPE0003");
        addresses.add(dropoffAddress);
        dropoffAddress = new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", "Customer 004", 38.251923, -85.800034, 840);
        dropoffAddress.setDropOff("PDA004");
        dropoffAddress.setAddressStopType(AddressStopType.DELIVERY.toString());
        dropoffAddress.setPieces(1);
        dropoffAddress.addCustomField("GOOD_TYPE", "TYPE0004");
        addresses.add(dropoffAddress);
        dropoffAddress = new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", "Customer 005", 38.176067, -85.824638, 840);
        dropoffAddress.setDropOff("PDA005");
        dropoffAddress.setAddressStopType(AddressStopType.DELIVERY.toString());
        dropoffAddress.addCustomField("GOOD_TYPE", "TYPE0005");
        dropoffAddress.setPieces(1);
        addresses.add(dropoffAddress);
        dropoffAddress = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", "Customer 006", 38.179806, -85.775558, 840);
        dropoffAddress.setDropOff("PDA006");
        dropoffAddress.setAddressStopType(AddressStopType.DELIVERY.toString());
        dropoffAddress.setPieces(1);
        dropoffAddress.addCustomField("GOOD_TYPE", "TYPE0006");
        addresses.add(dropoffAddress);
        dropoffAddress = new Address("318 SO. 39TH STREET, Louisville, KY, 40212", "Customer 007", 38.259335, -85.815094, 840);
        dropoffAddress.setDropOff("PDA007");
        dropoffAddress.setAddressStopType(AddressStopType.DELIVERY.toString());
        dropoffAddress.setPieces(1);
        dropoffAddress.addCustomField("GOOD_TYPE", "TYPE0007");
        addresses.add(dropoffAddress);
        optParameters.setAddresses(addresses);

        try {
            DataObject responseObject = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
            System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }
}
// codebeat:enable[SIMILARITY]
