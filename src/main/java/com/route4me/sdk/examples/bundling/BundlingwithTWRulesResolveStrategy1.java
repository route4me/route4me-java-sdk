package com.route4me.sdk.examples.bundling;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.*;

import java.util.ArrayList;
import java.util.List;

public class BundlingwithTWRulesResolveStrategy1 {

    public static void main(String[] args) {


        /*
        Bundling        
        time_window_rules: it’s the list of options about handling time windows (TW).
        time_window_rules.resolve_strategy (type: integer; available values: 0,1; default: 0) it’s the option to pick resolving TW strategy.
            The strategy 0 means the min/max strategy. Example: an address X has TW = [7AM, 10AM], an address Y has TW = [9AM, 11AM], in the end we will get TW = [min(7AM, 9AM), max(10AM, 11AM)] = [7AM, 11AM]
            The strategy 1 means the max/min strategy. Example: an address X has TW = [7AM, 10AM], an address Y has TW = [9AM, 11AM], in the end we will get TW = [max(7AM, 9AM), min(10AM, 11AM)] = [9AM, 10AM]
        time_window_rules.bundle_only_overlapping (type: boolean; default: false) it’s the option to control bunlding of overlapping TW. 
            Example: an address X has TW = [8AM, 10AM], an address Y has TW = [4PM, 6PM], so if the option is enabled these addresses won’t be bundled. 
            Note that this option will be automatically enabled if resolve_strategy = 1  (to avoid incorrect TW)
        time_window_rules.bundle_service_time_greater_than_tw (type: boolean; default: true) it’s the option to control bundling of stops with service-time (ST) > 0 and TW. 
            Example: an address X has TW = [7AM, 10AM]  and ST = 1 hour, an address Y has TW = [9AM, 11AM] and ST = 1 hour, with resolve_strategy = 1 we will get TW = [9AM, 10AM] and 
            the sum of service-times will be 1 + 1 = 2 hours, and 2 hours of ST is greater than 1 hour of TW, and in this case these addresses won’t be bundled. 
            In the same case these addresses can be bundled with  resolve_strategy = 0, because TW will be [7AM, 11AM] which means 4 hours, and 2 hours of ST is less than 4 hours of TW.
        */
        
        

        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(Constants.AlgorithmType.CVRP_TW_SD.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime(10 * 3600);

        parameters.setRt(Boolean.TRUE);
        parameters.setRouteMaxDuration(10 * 3600);
        parameters.setRouteName("Bundling Stops using Time Window Rules Resolve Strategy 1");
        parameters.setOptimize(Constants.Optimize.TIME.toString());
        parameters.setDistanceUnit(Constants.DistanceUnit.MI.toString());
        parameters.setDeviceType(Constants.DeviceType.WEB.toString());
        parameters.setTravelMode(Constants.TravelMode.DRIVING.toString());
        parameters.setParts(20);
        optParameters.setParameters(parameters);

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("455 S 4th St, Louisville, KY 40202", true, 38.251698, -85.75730, 0));
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", 38.141598, -85.793846, 300, 10 * 3600, 15 * 3600));

        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300, 11 * 3600, 12 * 3600));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300, 10 * 3600, 13 * 3600));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300, 9 * 3600, 14 * 3600));

        addresses.add(new Address("1661 W HILL ST, Louisville, KY, 40210", 38.229584, -85.783966, 300, 10 * 3600, 18 * 3600));
        addresses.add(new Address("1409 PHYLLIS AVE, Louisville, KY, 40215", 38.206154, -85.781387, 300, 10 * 3600, 18 * 3600));
        addresses.add(new Address("4504 SUNFLOWER AVE, Louisville, KY, 40216", 38.187511, -85.839149, 300, 11 * 3600,  18 * 3600));
        addresses.add(new Address("2512 GREENWOOD AVE, Louisville, KY, 40210", 38.241405, -85.795059, 300, 10 * 3600, 18 * 3600));
        addresses.add(new Address("5500 WILKE FARM AVE, Louisville, KY, 40216", 38.166065, -85.863319, 300, 11 * 3600, 18 * 3600));
        addresses.add(new Address("3640 LENTZ AVE, Louisville, KY, 40215", 38.193283, -85.786201, 300, 10 * 3600, 20 * 3600));
        addresses.add(new Address("1020 BLUEGRASS AVE, Louisville, KY, 40215", 38.17952, -85.780037, 300, 12 * 3600, 19 * 3600));
        addresses.add(new Address("123 NORTH 40TH ST, Louisville, KY, 40212", 38.26498, -85.814156, 300, 10 * 3600,  20 * 3600));


        Bundling bundling = new Bundling();
       
        bundling.setMergeMode(BundlingEnum.BundledItemsMode.KEEP_AS_SEPARATE_DESTINATIONS.getValue());
        
        bundling.setMode(BundlingEnum.BundlingMode.BUNDLING_BY_ADDRESS.getValue());
        ServiceTimeRules serviceTimeRules = new ServiceTimeRules();
        serviceTimeRules.setFirstItemMode(BundlingEnum.BundlingFirstItemMode.USE_CUSTOM_SERVICE_TIME.getValue());
        ArrayList<Integer> firstItemModeParams = new ArrayList<>();
        firstItemModeParams.add(600);
        serviceTimeRules.setFirstItemModeParams(firstItemModeParams);
        bundling.setServiceTimeRules(serviceTimeRules);
        TimeWindowRules timeWindowRules = new TimeWindowRules();
        timeWindowRules.setBundleOnlyOverlapping(Boolean.FALSE);
        timeWindowRules.setBundleServiceTimeGreaterThanTW(Boolean.TRUE);
        timeWindowRules.setResolveStrategy(1);
        bundling.setTimeWindowRules(timeWindowRules);
        parameters.setBundling(bundling);



        optParameters.setAddresses(addresses);

        try {
            DataObject responseObject = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
            System.out.println("State:" + Constants.OptimizationState.get(responseObject.getState().intValue()));
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
