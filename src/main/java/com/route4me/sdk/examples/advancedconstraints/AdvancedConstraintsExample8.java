/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples.advancedconstraints;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.route4me.sdk.examples.playground.AllocateTerritoriesToronto;
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

public class AdvancedConstraintsExample8 {

    public static List<Address> readAddressesFromCSV(String filename, List<String> zone1, List<String> zone2, List<String> zone3, Integer serviceTime) {
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
                addresses.add(address);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AllocateTerritoriesToronto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AllocateTerritoriesToronto.class.getName()).log(Level.SEVERE, null, ex);
        }

        return addresses;

    }

    //**********************************************************************
    // TEST CASE: Drivers Schedules with Territories
    // 30 Schedules
    // 3 Territories
    //**********************************************************************
    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        RoutingManager manager = new RoutingManager(apiKey, true);
        OptimizationParameters optParameters = new OptimizationParameters();

        // General Route Parameters
        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.ADVANCED_CVRP_TW.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRt(Boolean.TRUE);
        parameters.setParts(50); // Drivers Schedules Available 
        parameters.setRouteName("50 Drivers Schedules");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());

        // **********************
        // Schedules
        // **********************
        AdvancedConstraints schedule;
        List<String> zone = new ArrayList<>();
        List<AdvancedConstraints> advancedConstraints = new ArrayList<>();

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.toString(),"src", "main", "java", "com", "route4me", "sdk", "examples", "advancedconstraints", "schedules.csv");
        
        CSVReader csvReader;
        try {
            csvReader = new CSVReaderBuilder(new FileReader(filePath.toString())).withSkipLines(1).build();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                
            schedule = new AdvancedConstraints();
            int group = Integer.parseInt(nextRecord[2]);
            Integer timeStart = Integer.parseInt(nextRecord[0]);
            Integer timeEnd = Integer.parseInt(nextRecord[1]);
            
            schedule.setMembersCount(1);
            List<List<Integer>> timeWindowsSchedule = new ArrayList<>();
            List<Integer> timeWindowSchedule = Arrays.asList(timeStart, timeEnd);
            timeWindowsSchedule.add(timeWindowSchedule);
            schedule.setAvailableTimeWindows(timeWindowsSchedule);
            advancedConstraints.add(schedule);
                
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AllocateTerritoriesToronto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AllocateTerritoriesToronto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
        // Schedules registration
        parameters.setAdvancedConstraints(advancedConstraints);
        optParameters.setParameters(parameters);

        // Addresses
        List<Address> addresses = new ArrayList<>();


        // Depot    
        Address address = new Address("DEPOT", true, 25.694341, -80.166036, 0);
        address.setAlias("DEPOT");
        addresses.add(address);

        // Stops
        filePath = Paths.get(currentPath.toString(),"src", "main", "java", "com", "route4me", "sdk", "examples", "advancedconstraints", "locations.csv");

        Integer serviceTime = 120;       
        
        try {
            csvReader = new CSVReaderBuilder(new FileReader(filePath.toString())).withSkipLines(1).build();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                address = new Address();
                address.setAlias(nextRecord[0]);
                address.setAddress(nextRecord[0]);
                address.setLatitude(Double.parseDouble(nextRecord[1]));
                address.setLongitude(Double.parseDouble(nextRecord[2]));
                int group = Integer.parseInt(nextRecord[3]);
                addresses.add(address);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AllocateTerritoriesToronto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AllocateTerritoriesToronto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        


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
