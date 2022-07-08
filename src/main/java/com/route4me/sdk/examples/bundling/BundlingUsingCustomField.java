// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples.bundling;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants.*;
import com.route4me.sdk.services.routing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author juan
 */
public class BundlingUsingCustomField {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.CVRP_TW_SD.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteName("Single Depot, Multiple Driver, Bundling Any Custom Field");
        parameters.setTravelMode(TravelMode.DRIVING.toString());

        
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 300));
        addresses.add(new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300));
        addresses.add(new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300));
        addresses.add(new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300));
        addresses.add(new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300));
        addresses.add(new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300));
        addresses.add(new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300));
        
        Address address1 = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.162472, -85.792854, 300);
        Address address2 = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.162472, -85.792854, 300);
        Address address3 = new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.162472, -85.792854, 300);

        Map<String, Object> custom_fields = new HashMap<>();
        custom_fields.put("BUNDLING_KEY", "ZONE001");
        custom_fields.put("CUSTOM_MESSAGE", "Bundled Zones");

        address1.setCustom_fields(custom_fields);
        address2.setCustom_fields(custom_fields);

        addresses.add(address1);
        addresses.add(address2);
        addresses.add(address3);
        
        
        optParameters.setAddresses(addresses);
        

        Bundling bundling = new Bundling();

        bundling.setMergeMode(BundlingEnum.BundledItemsMode.KEEP_AS_SEPARATE_DESTINATIONS.getValue());

        bundling.setMode(BundlingEnum.BundlingMode.BUNDLING_BY_ANY_CUSTOM_DATA.getValue());
        List<String> modeParams = new ArrayList<>();
        modeParams.add("BUNDLING_KEY");
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
            System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
            if (responseObject.getAddresses() != null) {
                for (Address address : responseObject.getAddresses()) {
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
