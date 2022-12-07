/*
 * The MIT License
 *
 * Copyright 2022 Route4Me.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.route4me.sdk.examples.bundling;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Bundling;
import com.route4me.sdk.services.routing.BundlingEnum;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.ServiceTimeRules;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author route4me
 */
public class BundlingByAddressAndProrityConstraint {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey, true);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(Constants.AlgorithmType.CVRP_TW_SD.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteName("Single Depot, Multiple Driver, Bundling by Address and having Priority Constraint");
        parameters.setTravelMode(Constants.TravelMode.DRIVING.toString());

        List<Address> addresses = new ArrayList<>();
        // DEPOT
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 300));

        // Addresses
        addresses.add(new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300));
        addresses.add(new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300));
        addresses.add(new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300));

        // Bundling Addresses
        Address address;

        address = new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300);
        address.setPriority(1);
        addresses.add(address);
        address = new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300);
        address.setPriority(2);
        addresses.add(address);

        address = new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300);
        address.setPriority(2);
        addresses.add(address);
        address = new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300);
        address.setPriority(3);
        addresses.add(address);



        address = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300);
        address.setPriority(3);
        addresses.add(address);
        address = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300);
        address.setPriority(4);
        addresses.add(address);
        address = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300);
        address.setPriority(5);
        addresses.add(address);
        address = new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300);
        address.setPriority(6);
        

        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300);
        address.setPriority(4);
        addresses.add(address);
        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300);
        address.setPriority(5);
        addresses.add(address);
        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300);
        address.setPriority(6);
        addresses.add(address);
        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300);
        address.setPriority(7);
        addresses.add(address);
        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300);
        address.setPriority(8);
        addresses.add(address);
        address = new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300);
        address.setPriority(9);
        addresses.add(address);

        address = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.162472, -85.792854, 300);
        address.setPriority(5);
        addresses.add(address);
        address = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.162472, -85.792854, 300);
        address.setPriority(6);
        addresses.add(address);
        address = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.162472, -85.792854, 300);
        address.setPriority(7);
        addresses.add(address);
        address = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.162472, -85.792854, 300);
        address.setPriority(8);
        addresses.add(address);

        optParameters.setAddresses(addresses);

        Bundling bundling = new Bundling();

        bundling.setMergeMode(BundlingEnum.BundledItemsMode.KEEP_AS_SEPARATE_DESTINATIONS.getValue());

        bundling.setMode(BundlingEnum.BundlingMode.BUNDLING_BY_ADDRESS.getValue());
        List<String> modeParams = Arrays.asList("BUNDLING_KEY", "LOCATION_ID");
        bundling.setModeParams(modeParams);
        ServiceTimeRules serviceTimeRules = new ServiceTimeRules();
        serviceTimeRules.setFirstItemMode(BundlingEnum.BundlingFirstItemMode.USE_CUSTOM_SERVICE_TIME.getValue());
        serviceTimeRules.setAdditionalItemsMode(BundlingEnum.BundlingAdditionalItemMode.USE_CUSTOM_SERVICE_TIME_FOR_ADDITIONAL_ITEM.getValue());
        ArrayList<Integer> firstItemModeParams = new ArrayList();
        firstItemModeParams.add(900);
        ArrayList<Integer> additionalItemModeParams = new ArrayList();
        additionalItemModeParams.add(200);
        serviceTimeRules.setFirstItemModeParams(firstItemModeParams);
        serviceTimeRules.setAdditionalItemsModeParams(additionalItemModeParams);
        bundling.setServiceTimeRules(serviceTimeRules);
        parameters.setBundling(bundling);

        optParameters.setParameters(parameters);

        try {
            DataObject responseObject = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
            System.out.println("State:" + Constants.OptimizationState.get(responseObject.getState().intValue()));
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }

    }

}
