package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.enums.Constants.*;
import com.route4me.sdk.serdes.DataObjectDeserializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author juan
 */
public class SingleDriverRoundTrip {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        OptimizationManager optimizationManager = route4me.getOptimizationManager();
        Map<String, String> params = new HashMap<>();
        optimizationManager.setParams(params);
        DataObject data = new DataObject();
        Parameters parameters = new Parameters();
        List<Address> addresses = new ArrayList<>();
        data.setParameters(parameters);
        data.setAddresses(addresses);
        parameters.setAlgorithmType(AlgorithmType.TSP.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime(0);
        parameters.setRouteMaxDuration(86400);
        parameters.setVehicleCapacity("1");
        parameters.setVehicleMaxDistanceMi("10000");
        parameters.setRouteName("Single Driver Round Trip");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());
        addresses.add(new Address("754 5th Ave New York, NY 10019", Boolean.TRUE,
                "Bergdorf Goodman", 40.7636197, -73.9744388, 0));
        addresses.add(new Address("717 5th Ave New York, NY 10022", "Giorgio Armani",
                40.7669692, -73.9693864, 0));
        addresses.add(new Address("888 Madison Ave New York, NY 10014", "Ralph Lauren Women's and Home",
                40.7715154, -73.9669241, 0));
        addresses.add(new Address("1011 Madison Ave New York, NY 10075", "Yigal Azrou'l",
                40.7772129, -73.9669, 0));
        addresses.add(new Address("440 Columbus Ave New York, NY 10024", "Frank Stella Clothier",
                40.7808364, -73.9732729, 0));
        addresses.add(new Address("324 Columbus Ave #1 New York, NY 10023", "Liana",
                40.7803123, -73.9793079, 0));
        addresses.add(new Address("110 W End Ave New York, NY 10023", "Toga Bike Shop",
                40.7753077, -73.9861529, 0));
        addresses.add(new Address("555 W 57th St New York, NY 10019", "BMW of Manhattan",
                40.7718005, -73.9897716, 0));
        addresses.add(new Address("57 W 57th St New York, NY 10019", "Verizon Wireless",
                40.7558695, -73.9862019, 0));
        optimizationManager.setData(data);
        DataObject responseObject = optimizationManager.runOptimization();
        System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
        System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
        if (responseObject.getAddresses() != null) {
            for (Address address : responseObject.getAddresses()) {
                System.out.println("Address:" + address.getAddress());
                System.out.println("Route ID:" + address.getRouteId());
            }
        }
    }

}
