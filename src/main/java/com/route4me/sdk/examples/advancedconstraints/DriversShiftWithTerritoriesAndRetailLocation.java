/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.advancedconstraints;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
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
import com.route4me.sdk.services.routing.override.addresses.OverrideAddresses;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriversShiftWithTerritoriesAndRetailLocation {

    public static List<Address> readAddressesFromCSV(String filename, List<String> territory1, List<String> territory2, List<String> territory3, Integer serviceTime) {
        List<Address> addresses = new ArrayList<>();
        CSVReader csvReader;
        try {
            csvReader = new CSVReaderBuilder(new FileReader(filename)).withSkipLines(1).build();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                Address address = new Address();
                address.setAlias(nextRecord[0]);
                address.setAddress(nextRecord[0]);
                address.setLatitude(Double.parseDouble(nextRecord[1]));
                address.setLongitude(Double.parseDouble(nextRecord[2]));
                int group = Integer.parseInt(nextRecord[3]);

                switch (group) {
                    case 0:
                        address.setTags(territory1);
                        break;
                    case 1:
                        address.setTags(territory2);
                        break;
                    case 2:
                        address.setTags(territory3);
                        break;
                }
                addresses.add(address);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DriversShiftWithTerritoriesAndRetailLocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DriversShiftWithTerritoriesAndRetailLocation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return addresses;

    }

    //**********************************************************************
    // TEST CASE: Drivers Schedules with Territories and Retail Location
    // 2000 Stops
    // 2 Driver Shifts:
    //    Full Time 8h 
    //    Part Time 4h 
    // 3 Territories
    // Retail Location
    //**********************************************************************
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey, Boolean.TRUE);
        OptimizationParameters optParameters = new OptimizationParameters();

        // General Route Parameters
        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.ADVANCED_CVRP_TW.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRt(Boolean.TRUE);
        parameters.setParts(20);
        parameters.setRouteName("Drivers Shift - 3 Territories - Retail Location");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());
        OverrideAddresses override = new OverrideAddresses();
        override.setTime(90);
        parameters.setOverrideAddresses(override);

        // **********************
        // Territories
        // **********************
        List<String> territory1 = Arrays.asList("TERRITORY 01");
        List<String> territory2 = Arrays.asList("TERRITORY 02");
        List<String> territory3 = Arrays.asList("TERRITORY 03");

        // **********************
        // Schedules
        // **********************
        AdvancedConstraints schedule;
        List<AdvancedConstraints> advancedConstraints = new ArrayList<>();

        //Retail Location
        Address retailLocationAddress = new Address("RETAIL LOCATION", "RETAIL LOCATION", 25.8741751, -80.1288583, 300);

        List<Object> locationsSequence = Arrays.asList("", retailLocationAddress);

        List<List<String>> territories = Arrays.asList(territory1, territory2, territory3);

        Map<String, List<Integer>> shifts = new HashMap<>();

        shifts.put("full_time", Arrays.asList(13 * 3600, 21 * 3600)); // 8H
        shifts.put("part_time", Arrays.asList(13 * 3600, 17 * 3600)); // 4H

        for (String shift : shifts.keySet()) {
            for (List<String> territory : territories) {
                schedule = new AdvancedConstraints();
                schedule.setGroup(shift);
                schedule.setTags(territory);
                schedule.setMembersCount(20);
                List<List<Integer>> timeWindowsSchedule = new ArrayList<>();
                List<Integer> timeWindowSchedule = shifts.get(shift);
                timeWindowsSchedule.add(timeWindowSchedule);
                schedule.setAvailableTimeWindows(timeWindowsSchedule);
                schedule.setLocationSequencePattern(locationsSequence);
                advancedConstraints.add(schedule);
            }
        }

        // Schedules registration
        parameters.setAdvancedConstraints(advancedConstraints);

        List<List<Object>> groups = new ArrayList<>();

        groups.add(Arrays.asList("full_time", 4));
        groups.add(Arrays.asList("part_time", 16));

        parameters.setGroupMaxRoutes(groups);
        optParameters.setParameters(parameters);

        // Addresses
        List<Address> addresses;
        Integer serviceTime = 300;
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.toString(), "src", "main", "java", "com", "route4me", "sdk", "examples", "advancedconstraints", "locations.csv");

        addresses = readAddressesFromCSV(filePath.toString(), territory1, territory2, territory3, serviceTime);

        // Depot
        Address address = new Address("DEPOT", true, 25.723025, -80.452883, 0);
        addresses.add(address);

        // Stops
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
