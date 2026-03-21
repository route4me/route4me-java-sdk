package com.route4me.sdk.examples;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants.*;
import com.route4me.sdk.services.routing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultipleBreaksOptimization {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.CVRP_TW_SD.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime(25200);
        parameters.setRouteMaxDuration(86400);
        parameters.setVehicleCapacity("100");
        parameters.setVehicleMaxDistanceMi("10000");
        parameters.setRouteName("Multiple Breaks Example");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());

        // Configure multiple breaks
        List<Break> breaks = new ArrayList<>();

        // Break 1: 10-minute break every 5 minutes
        Break break1 = new Break();
        break1.setMode(0);
        break1.setModeParams(Arrays.asList(300));
        break1.setDuration(600);
        break1.setRepeatsNumber(1);
        breaks.add(break1);

        // Break 2: 30-minute break every 5 minutes
        Break break2 = new Break();
        break2.setMode(0);
        break2.setModeParams(Arrays.asList(300));
        break2.setDuration(1800);
        break2.setRepeatsNumber(1);
        breaks.add(break2);

        // Break 3: 10-minute break every 5 minutes
        Break break3 = new Break();
        break3.setMode(0);
        break3.setModeParams(Arrays.asList(300));
        break3.setDuration(600);
        break3.setRepeatsNumber(1);
        breaks.add(break3);

        parameters.setBreaks(breaks);
        optParameters.setParameters(parameters);

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("754 5th Ave New York, NY 10019", true, "Bergdorf Goodman", 40.7636197, -73.9744388, 0));
        addresses.add(new Address("717 5th Ave New York, NY 10022", "Giorgio Armani", 40.7669692, -73.9693864, 0));
        addresses.add(new Address("888 Madison Ave New York, NY 10014", "Ralph Lauren Women's and Home", 40.7715154, -73.9669241, 0));
        addresses.add(new Address("1011 Madison Ave New York, NY 10075", "Yigal Azrou'l", 40.7772129, -73.9669, 0));
        addresses.add(new Address("440 Columbus Ave New York, NY 10024", "Frank Stella Clothier", 40.7808364, -73.9732729, 0));
        addresses.add(new Address("324 Columbus Ave #1 New York, NY 10023", "Liana", 40.7803123, -73.9793079, 0));
        addresses.add(new Address("110 W End Ave New York, NY 10023", "Toga Bike Shop", 40.7753077, -73.9861529, 0));
        addresses.add(new Address("555 W 57th St New York, NY 10019", "BMW of Manhattan", 40.7718005, -73.9897716, 0));
        addresses.add(new Address("57 W 57th St New York, NY 10019", "Verizon Wireless", 40.7558695, -73.9862019, 0));
        optParameters.setAddresses(addresses);

        try {
            DataObject responseObject = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID: " + responseObject.getOptimizationProblemId());
            Number state = responseObject.getState();
            if (state != null) {
                System.out.println("State: " + OptimizationState.get(state.intValue()));
            }
            if (responseObject.getAddresses() != null) {
                for (Address address : responseObject.getAddresses()) {
                    System.out.println(address);
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

}
