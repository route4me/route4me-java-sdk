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

    /** The stops: alias, latitude, longitude and zone (1 to 3). */
    private static final Object[][] STOPS = {
        {"2158", 25.603049, -80.348022, 1},
        {"253", 25.618737, -80.329138, 1},
        {"1527", 25.660645, -80.284289, 2},
        {"1889", 25.816771, -80.265282, 2},
        {"3998", 25.830834, -80.336474, 2},
        {"1988", 25.934509, -80.216283, 3},
        {"3566", 25.826221, -80.247753, 3},
        {"2659", 25.60218, -80.384538, 1},
        {"2477", 25.679245, -80.281254, 2},
        {"3968", 25.655636, -80.350484, 1},
        {"1700", 25.871786, -80.341298, 2},
        {"1804", 25.690688, -80.318216, 2},
        {"209", 25.893571, -80.20119, 3},
        {"834", 25.951618, -80.29993, 3},
        {"1530", 25.818694, -80.354931, 2},
        {"1696", 25.748019, -80.243968, 2},
        {"1596", 25.834085, -80.193554, 3},
        {"3563", 25.690451, -80.272227, 2},
        {"3622", 25.602187, -80.411931, 1},
        {"1805", 25.780564, -80.415264, 2},
        {"1655", 25.779567, -80.356258, 2},
        {"1533", 25.459839, -80.44416, 1},
        {"269", 25.777716, -80.25451, 2},
        {"1238", 25.821602, -80.12694, 3},
        {"3312", 25.894716, -80.33056, 3},
        {"3989", 25.553594, -80.374832, 1},
        {"457", 25.636562, -80.451262, 1},
        {"3105", 25.737308, -80.43438, 2},
        {"3317", 25.752353, -80.215284, 2},
        {"3589", 25.877066, -80.22757, 3},
        {"3313", 25.93445, -80.257547, 3},
        {"385", 25.902516, -80.254678, 3},
        {"2601", 25.85515, -80.219475, 3},
        {"602", 25.513304, -80.387233, 1},
        {"2710", 25.626475, -80.428484, 1},
        {"1187", 25.781259, -80.402599, 2},
        {"2155", 25.760206, -80.330144, 2},
        {"990", 25.9182, -80.352967, 3},
        {"2810", 25.763907, -80.293502, 2},
        {"3294", 25.576745, -80.380201, 1},
        {"3578", 25.441741, -80.454027, 1},
        {"1040", 25.741545, -80.320633, 2},
        {"2184", 25.769002, -80.404676, 2},
        {"842", 25.705431, -80.398938, 1},
        {"1668", 25.770751, -80.21817, 2},
        {"2603", 25.660366, -80.376896, 1},
        {"1304", 25.935256, -80.176192, 3},
        {"3281", 25.962562, -80.250286, 3},
        {"2855", 25.781819, -80.235649, 2},
        {"2518", 25.632515, -80.368998, 1},
        {"46", 25.741641, -80.221332, 2},
        {"3185", 25.945872, -80.310623, 3},
        {"3309", 25.761921, -80.368253, 2},
        {"2586", 25.792323, -80.336024, 2},
        {"237", 25.749872, -80.393815, 2},
        {"2192", 25.94228, -80.174436, 3},
        {"2887", 25.753024, -80.232491, 2},
        {"3485", 25.547749, -80.375777, 1},
        {"3832", 25.489671, -80.419657, 1},
        {"1393", 25.872401, -80.295227, 3},
        {"781", 25.912158, -80.204096, 3},
        {"2165", 25.745813, -80.275891, 2},
        {"537", 25.843267, -80.373141, 2},
        {"235", 25.877239, -80.222824, 3},
        {"1175", 25.924446, -80.162018, 3},
        {"2235", 25.850434, -80.183362, 3},
        {"2775", 25.647769, -80.410684, 1},
        {"1556", 25.457798, -80.483813, 1},
        {"3233", 25.593026, -80.382412, 1},
        {"3534", 25.867923, -80.24087, 3},
        {"3205", 25.656392, -80.291358, 1},
        {"2893", 25.867024, -80.201303, 3},
        {"1555", 25.776622, -80.415111, 2},
        {"3218", 25.832436, -80.280374, 2},
        {"899", 25.855764, -80.187256, 3},
        {"1027", 25.735087, -80.259917, 2},
        {"3448", 25.84728, -80.266024, 3},
        {"884", 25.480335, -80.458004, 1},
        {"448", 25.684473, -80.451831, 1},
        {"3643", 25.677524, -80.425454, 1},
        {"1952", 25.754493, -80.342664, 2},
        {"3507", 25.874399, -80.345727, 2},
        {"3520", 25.483806, -80.428498, 1},
        {"1611", 25.713302, -80.440269, 1},
        {"1402", 25.72308, -80.444812, 1},
        {"1211", 25.699226, -80.422237, 1},
        {"1161", 25.835215, -80.252216, 3},
        {"1274", 25.888309, -80.344764, 3},
        {"1341", 25.716966, -80.438179, 1},
        {"2946", 25.530972, -80.448924, 1},
        {"813", 25.488095, -80.450334, 1},
        {"3862", 25.954786, -80.16335, 3},
        {"759", 25.555122, -80.335284, 1},
        {"52", 25.927916, -80.268189, 3},
        {"909", 25.832815, -80.217156, 3},
        {"2768", 25.835259, -80.223997, 3},
        {"3967", 25.630732, -80.366065, 1},
        {"1974", 25.931024, -80.217991, 3},
        {"3147", 25.921095, -80.261839, 3}
    };

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
        List<Integer> timeWindowSchedule1 = Arrays.asList((8 + 5) * 3600, (11 + 5) * 3600);
        timeWindowsSchedule1.add(timeWindowSchedule1);
        schedule1.setAvailableTimeWindows(timeWindowsSchedule1);


        // Schedule 2
        // Time Window Start:  8:00 am EST
        // Time Window End:   12:00 pm EST
        AdvancedConstraints schedule2 = new AdvancedConstraints();
        schedule2.setTags(zone2);
        schedule2.setMembersCount(4);
        List<List<Integer>> timeWindowsSchedule2 = new ArrayList<>();
        List<Integer> timeWindowSchedule2 = Arrays.asList((8 + 5) * 3600, (12 + 5) * 3600);
        timeWindowsSchedule2.add(timeWindowSchedule2);
        schedule2.setAvailableTimeWindows(timeWindowsSchedule2);


        // Schedule 3
        // Time Window Start:  8:00 am EST
        // Time Window End:    01:00 pm EST
        AdvancedConstraints schedule3 = new AdvancedConstraints();
        schedule3.setTags(zone3);
        schedule3.setMembersCount(3);
        List<List<Integer>> timeWindowsSchedule3 = new ArrayList<>();
        List<Integer> timeWindowSchedule3 = Arrays.asList((8 + 5) * 3600, (13 + 5) * 3600);
        timeWindowsSchedule3.add(timeWindowSchedule3);
        schedule3.setAvailableTimeWindows(timeWindowsSchedule3);


        // Schedules registration
        List<AdvancedConstraints> advancedConstraints = Arrays.asList(schedule1, schedule2, schedule3);
        parameters.setAdvancedConstraints(advancedConstraints);
        optParameters.setParameters(parameters);

        // Addresses

        List<Address> addresses = buildAddresses(zone1, zone2, zone3);
        optParameters.setAddresses(addresses);

        try {
            DataObject responseObject = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
            System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
            if (responseObject.getAddresses() != null) {
                for (Iterator<Address> it = responseObject.getAddresses().iterator(); it.hasNext();) {
                    Address address = it.next();
                    System.out.println(address);
                }
            }
        } catch (APIException e) {
            //handle exception
            e.printStackTrace();
        }
    }

    /**
     * Builds the depot and the stops of {@link #STOPS}, each tagged with the zone it belongs to.
     *
     * @param zone1 the tags of the stops in zone 1
     * @param zone2 the tags of the stops in zone 2
     * @param zone3 the tags of the stops in zone 3
     * @return the addresses of the optimization
     */
    private static List<Address> buildAddresses(List<String> zone1, List<String> zone2, List<String> zone3) {
        List<Address> addresses = new ArrayList<>();
        // Depot
        addresses.add(new Address("DEPOT", true, 25.723025, -80.452883, 0));
        // Stops
        Integer serviceTime = 300;
        List<List<String>> zones = Arrays.asList(zone1, zone2, zone3);
        for (Object[] stop : STOPS) {
            Address address = new Address((String) stop[0], false, (Double) stop[1], (Double) stop[2], serviceTime);
            address.setTags(zones.get((Integer) stop[3] - 1));
            addresses.add(address);
        }
        return addresses;
    }



}
