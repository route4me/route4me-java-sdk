/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.advancedconstraints;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Constants.AlgorithmType;
import com.route4me.sdk.services.routing.Constants.DeviceType;
import com.route4me.sdk.services.routing.Constants.DistanceUnit;
import com.route4me.sdk.services.routing.Constants.OptimizationState;
import com.route4me.sdk.services.routing.Constants.Optimize;
import com.route4me.sdk.services.routing.Constants.TravelMode;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.advancedconstraints.AdvancedConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class DriversScheduleswithTerritoriesV4 {

    //**********************************************************************
    // TEST CASE: Drivers Schedules with Territories
    // 10 Drivers
    // 3 Schedules
    // 3 Territories
    //**********************************************************************

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();
        
        // General Route Parameters
        
        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.ADVANCED_CVRP_TW.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRt(Boolean.TRUE);
        parameters.setRouteName("Drivers Schedules - 3 Territories");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());

        // **********************
        // Territories
        // **********************
        List<String> zone1 = Arrays.asList("ZONE 01");
        List<String> zone2 = Arrays.asList("ZONE 02");
        List<String> zone3 = Arrays.asList("ZONE 03");
        
        // **********************
        // Schedules
        // **********************

        // Schedule 1
        // Time Window Start:  8:00 am EST
        // Time Window End:   11:00 am EST
        AdvancedConstraints schedule1 = new AdvancedConstraints();
        schedule1.setTags(zone1);
        schedule1.setMembersCount(3);
        List<List<Integer>> timeWindowsSchedule1 = new ArrayList<>();
        List<Integer> timeWindowSchedule1 = Arrays.asList((8 + 5) * 3600 , (11 + 5) * 3600);
        timeWindowsSchedule1.add(timeWindowSchedule1);
        schedule1.setAvailableTimeWindows(timeWindowsSchedule1);
        

        // Schedule 2
        // Time Window Start:  8:00 am EST
        // Time Window End:   12:00 pm EST
        AdvancedConstraints schedule2 = new AdvancedConstraints();
        schedule2.setTags(zone2);
        schedule2.setMembersCount(4);
        List<List<Integer>> timeWindowsSchedule2 = new ArrayList<>();
        List<Integer> timeWindowSchedule2 = Arrays.asList((8 + 5) * 3600 , (12 + 5) * 3600);
        timeWindowsSchedule2.add(timeWindowSchedule2);
        schedule2.setAvailableTimeWindows(timeWindowsSchedule2);
        

        // Schedule 3
        // Time Window Start:  8:00 am EST
        // Time Window End:    01:00 pm EST
        AdvancedConstraints schedule3 = new AdvancedConstraints();
        schedule3.setTags(zone3);
        schedule3.setMembersCount(3);
        List<List<Integer>> timeWindowsSchedule3 = new ArrayList<>();
        List<Integer> timeWindowSchedule3 = Arrays.asList((8 + 5) * 3600 , (13 + 5) * 3600);
        timeWindowsSchedule3.add(timeWindowSchedule3);
        schedule3.setAvailableTimeWindows(timeWindowsSchedule3);
        

        // Schedules registration
        List<AdvancedConstraints> advancedConstraints = Arrays.asList(schedule1, schedule2, schedule3);       
        parameters.setAdvancedConstraints(advancedConstraints);
        optParameters.setParameters(parameters);

        // Addresses
        
        List<Address> addresses = new ArrayList<>();
        // Depot

        Address address = new Address("DEPOT", true, 25.723025, -80.452883, 0);
        addresses.add(address);

        // Stops
        
        Integer serviceTime = 300;
        
        address = new Address("2158", false, 25.603049, -80.348022, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("253", false, 25.618737, -80.329138, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1527", false, 25.660645, -80.284289, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("1889", false, 25.816771, -80.265282, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3998", false, 25.830834, -80.336474, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("1988", false, 25.934509, -80.216283, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3566", false, 25.826221, -80.247753, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("2659", false, 25.60218, -80.384538, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("2477", false, 25.679245, -80.281254, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3968", false, 25.655636, -80.350484, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1700", false, 25.871786, -80.341298, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("1804", false, 25.690688, -80.318216, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("209", false, 25.893571, -80.20119, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("834", false, 25.951618, -80.29993, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("1530", false, 25.818694, -80.354931, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("1696", false, 25.748019, -80.243968, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("1596", false, 25.834085, -80.193554, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3563", false, 25.690451, -80.272227, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3622", false, 25.602187, -80.411931, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1805", false, 25.780564, -80.415264, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("1655", false, 25.779567, -80.356258, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("1533", false, 25.459839, -80.44416, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("269", false, 25.777716, -80.25451, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("1238", false, 25.821602, -80.12694, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3312", false, 25.894716, -80.33056, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3989", false, 25.553594, -80.374832, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("457", false, 25.636562, -80.451262, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("3105", false, 25.737308, -80.43438, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3317", false, 25.752353, -80.215284, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3589", false, 25.877066, -80.22757, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3313", false, 25.93445, -80.257547, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("385", false, 25.902516, -80.254678, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("2601", false, 25.85515, -80.219475, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("602", false, 25.513304, -80.387233, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("2710", false, 25.626475, -80.428484, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1187", false, 25.781259, -80.402599, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("2155", false, 25.760206, -80.330144, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("990", false, 25.9182, -80.352967, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("2810", false, 25.763907, -80.293502, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3294", false, 25.576745, -80.380201, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("3578", false, 25.441741, -80.454027, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1040", false, 25.741545, -80.320633, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("2184", false, 25.769002, -80.404676, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("842", false, 25.705431, -80.398938, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1668", false, 25.770751, -80.21817, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("2603", false, 25.660366, -80.376896, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1304", false, 25.935256, -80.176192, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3281", false, 25.962562, -80.250286, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("2855", false, 25.781819, -80.235649, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("2518", false, 25.632515, -80.368998, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("46", false, 25.741641, -80.221332, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3185", false, 25.945872, -80.310623, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3309", false, 25.761921, -80.368253, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("2586", false, 25.792323, -80.336024, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("237", false, 25.749872, -80.393815, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("2192", false, 25.94228, -80.174436, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("2887", false, 25.753024, -80.232491, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3485", false, 25.547749, -80.375777, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("3832", false, 25.489671, -80.419657, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1393", false, 25.872401, -80.295227, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("781", false, 25.912158, -80.204096, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("2165", false, 25.745813, -80.275891, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("537", false, 25.843267, -80.373141, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("235", false, 25.877239, -80.222824, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("1175", false, 25.924446, -80.162018, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("2235", false, 25.850434, -80.183362, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("2775", false, 25.647769, -80.410684, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1556", false, 25.457798, -80.483813, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("3233", false, 25.593026, -80.382412, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("3534", false, 25.867923, -80.24087, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3205", false, 25.656392, -80.291358, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("2893", false, 25.867024, -80.201303, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("1555", false, 25.776622, -80.415111, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3218", false, 25.832436, -80.280374, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("899", false, 25.855764, -80.187256, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("1027", false, 25.735087, -80.259917, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3448", false, 25.84728, -80.266024, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("884", false, 25.480335, -80.458004, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("448", false, 25.684473, -80.451831, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("3643", false, 25.677524, -80.425454, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1952", false, 25.754493, -80.342664, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3507", false, 25.874399, -80.345727, serviceTime);
        address.setTags(zone2);
        addresses.add(address);
        address = new Address("3520", false, 25.483806, -80.428498, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1611", false, 25.713302, -80.440269, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1402", false, 25.72308, -80.444812, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1211", false, 25.699226, -80.422237, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1161", false, 25.835215, -80.252216, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("1274", false, 25.888309, -80.344764, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("1341", false, 25.716966, -80.438179, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("2946", false, 25.530972, -80.448924, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("813", false, 25.488095, -80.450334, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("3862", false, 25.954786, -80.16335, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("759", false, 25.555122, -80.335284, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("52", false, 25.927916, -80.268189, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("909", false, 25.832815, -80.217156, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("2768", false, 25.835259, -80.223997, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3967", false, 25.630732, -80.366065, serviceTime);
        address.setTags(zone1);
        addresses.add(address);
        address = new Address("1974", false, 25.931024, -80.217991, serviceTime);
        address.setTags(zone3);
        addresses.add(address);
        address = new Address("3147", false, 25.921095, -80.261839, serviceTime);
        address.setTags(zone3);
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
