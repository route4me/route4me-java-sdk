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
import com.route4me.sdk.services.routing.Constants.OptimizationState;
import com.route4me.sdk.services.routing.Constants.TravelMode;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.routing.advancedconstraints.AdvancedConstraints;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriversDifferentWorkingTimeWithTerritories {

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
            Logger.getLogger(DriversDifferentWorkingTimeWithTerritories.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DriversDifferentWorkingTimeWithTerritories.class.getName()).log(Level.SEVERE, null, ex);
        }

        return addresses;

    }

    //**********************************************************************
    // TEST CASE: Driver's Different Working Time
    // Drivers have 2 Working Times:
    // Full Time 8h
    // Partial Time 4h
    // 2000 Stops
    // 3 Territories
    //**********************************************************************
    public static void main(String[] args) {
        String apiKey = "EB6B3F9EC63324BEB0A1432697310BD4";
        RoutingManager manager = new RoutingManager(apiKey, Boolean.TRUE);
        OptimizationParameters optParameters = new OptimizationParameters();

        // General Route Parameters
        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.ADVANCED_CVRP_TW.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRt(Boolean.TRUE);
        parameters.setParts(30);
        parameters.setRouteName("Drivers Different Working Time Example - Territories");
        parameters.setTravelMode(TravelMode.DRIVING.toString());

        // **********************
        // Territories
        // **********************
        List<String> territory1 = Arrays.asList("ZONE 01");
        List<String> territory2 = Arrays.asList("ZONE 02");
        List<String> territory3 = Arrays.asList("ZONE 03");

        // **********************
        // Schedules
        // **********************
        AdvancedConstraints schedule;
        List<String> territory = new ArrayList<>();
        List<AdvancedConstraints> advancedConstraints = new ArrayList<>();

        Integer memberCount = 0;
        Integer timeWindowEnd = 0;
        
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    territory = territory1;
                    break;
                case 1:
                    territory = territory2;
                    break;
                case 2:
                    territory = territory3;
                    break;
            }

            for (int j = 0; j < 2; j++) {
                switch (j) {
                    case 0:
                        memberCount = 10;
                        timeWindowEnd = (11 + 5) * 3600; //Part Time
                        break;
                    case 1:
                        memberCount = 20;
                        timeWindowEnd = (15 + 5) * 3600; //Full Time
                        break;
                }
                schedule = new AdvancedConstraints();
                schedule.setTags(territory);
                schedule.setMembersCount(memberCount);
                List<List<Integer>> timeWindowsSchedule = new ArrayList<>();
                List<Integer> timeWindowSchedule = Arrays.asList((7 + 5) * 3600, timeWindowEnd);
                timeWindowsSchedule.add(timeWindowSchedule);
                schedule.setAvailableTimeWindows(timeWindowsSchedule);

                advancedConstraints.add(schedule);
            }
        }

        // Schedules registration
        parameters.setAdvancedConstraints(advancedConstraints);
        optParameters.setParameters(parameters);

        // Addressesjy4
        List<Address> addresses;
        // Depot

        Integer serviceTime = 300;

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.toString(),"src", "main", "java", "com", "route4me", "sdk", "examples", "advancedconstraints", "locations.csv");

        
        addresses = readAddressesFromCSV(filePath.toString(), territory1, territory2, territory3, serviceTime);
        
        Address address = new Address("DEPOT", true, 25.723025, -80.452883, 0);
        addresses.add(address);

        // Stops
        
        


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
