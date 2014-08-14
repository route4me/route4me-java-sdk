package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.model.Address;
import com.route4me.sdk.model.Base;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Optimization;
import com.route4me.sdk.model.Parameters;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Routes;
import com.route4me.sdk.model.enums.Constants.*;
import com.route4me.sdk.model.enums.Constants.DeviceType;
import com.route4me.sdk.model.enums.Constants.DistanceUnit;
import com.route4me.sdk.model.enums.Constants.Metric;
import com.route4me.sdk.model.enums.Constants.OptimizationState;
import com.route4me.sdk.model.enums.Constants.Optimize;
import com.route4me.sdk.model.enums.Constants.TravelMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author juan
 */
public class SingleDepotMultipleDriver {

    public static void main(String[] args) {
        String apiKey = "11111111111111111111111111111111";
        Route4Me route4me = new Route4Me(apiKey);
        Optimization optimization = route4me.getOptimization();
        Map<String, String> params = new HashMap<>();
        optimization.setParams(params);
        DataObject data = new DataObject();
        Parameters parameters = new Parameters();
        List<Address> addresses = new ArrayList<>();
        data.setParameters(parameters);
        data.setAddresses(addresses);
        parameters.setAlgorithm_type("101");
        parameters.setStore_route(Boolean.FALSE);
        parameters.setShare_route(Boolean.FALSE);
        parameters.setRoute_time(0);
        parameters.setParts(3);
        parameters.setRoute_max_duration(86400);
        parameters.setVehicle_capacity("100");
        parameters.setVehicle_max_distance_mi("10000");
        parameters.setRoute_name("Single Depot, Multiple Driver");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistance_unit(DistanceUnit.MI.toString());
        parameters.setDevice_type(DeviceType.WEB.toString());
        parameters.setTravel_mode(TravelMode.DRIVING.toString());
        parameters.setMetric(Metric.ROUTE4ME_METRIC_GEODESIC.getValue());
        addresses.add(new Address("3634 W Market St, Fairlawn, OH 44333", Boolean.TRUE,
                41.135762259364, -81.629313826561, 300, 28800, 29465));
        addresses.add(new Address("1218 Ruth Ave, Cuyahoga Falls, OH 44221",
                41.143505096435, -81.46549987793, 300, 29465, 30529));
        addresses.add(new Address("512 Florida Pl, Barberton, OH 44203",
                41.003671512008, -81.598461046815, 300, 30529, 33779));
        addresses.add(new Address("512 Florida Pl, Barberton, OH 44203",
                41.003671512008, -81.598461046815, 300, 33779, 33944));
        addresses.add(new Address("3495 Purdue St, Cuyahoga Falls, OH 44221",
                41.162971496582, -81.479049682617, 300, 33944, 34801));
        addresses.add(new Address("1659 Hibbard Dr, Stow, OH 44224",
                41.194505989552, -81.443351581693, 300, 34801, 36366));
        addresses.add(new Address("2705 N River Rd, Stow, OH 44224",
                41.145240783691, -81.410247802734, 300, 36366, 39173));
        addresses.add(new Address("10159 Bissell Dr, Twinsburg, OH 44087",
                41.340042114258, -81.421226501465, 300));
        addresses.add(new Address("367 Cathy Dr, Munroe Falls, OH 44262",
                41.148578643799, -81.429229736328, 300));
        addresses.add(new Address("367 Cathy Dr, Munroe Falls, OH 44262",
                41.148578643799, -81.429229736328, 300));
        addresses.add(new Address("512 Florida Pl, Barberton, OH 44203",
                41.003671512008, -81.598461046815, 300));
        addresses.add(new Address("559 W Aurora Rd, Northfield, OH 44067",
                41.315116882324, -81.558746337891, 300));
        addresses.add(new Address("3933 Klein Ave, Stow, OH 44224",
                41.169467926025, -81.429420471191, 300));
        addresses.add(new Address("2148 8th St, Cuyahoga Falls, OH 44221",
                41.136692047119, -81.493492126465, 300));
        addresses.add(new Address("3731 Osage St, Stow, OH 44224",
                41.161357879639, -81.42293548584, 300));
        addresses.add(new Address("3731 Osage St, Stow, OH 44224",
                41.161357879639, -81.42293548584, 300, 52180, 54379));
        route4me.getOptimization().setData(data);
        Response response = route4me.runOptimization();
        DataObject responseObject = Base.GSONDeserializer.fromJson(response.getResponseBody(), DataObject.class);
        String jsonResponse = Base.GSONSerializer.toJson(responseObject);
        System.out.println(jsonResponse);
        System.out.println("Response Code:" + response.getResponseCode());
        System.out.println("Optimization Problem ID:" + responseObject.getOptimization_problem_id());
        System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
        if (responseObject.getRoutes() != null) {
            if (responseObject.getRoutes() != null) {
                for (Routes route : responseObject.getRoutes()) {
                    System.out.println();
                    System.out.println("Route Name: " + route.getParameters().getRoute_name());
                    System.out.println("Route ID: " + route.getRoute_id());
                    if (route.getAddresses() != null) {
                        for (Address address : route.getAddresses()) {
                            System.out.println("Address:" + address.getAddress());
                        }
                    }
                }
            }
        }
    }
}
