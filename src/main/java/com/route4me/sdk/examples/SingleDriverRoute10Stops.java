package com.route4me.sdk.examples;

import com.route4me.sdk.Route4Me;
import com.route4me.sdk.model.Address;
import com.route4me.sdk.model.Base;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Optimization;
import com.route4me.sdk.model.Parameters;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.enums.Constants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author juan
 */
public class SingleDriverRoute10Stops {

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
        parameters.setAlgorithm_type(AlgorithmType.TSP.getValue());
        parameters.setStore_route(Boolean.FALSE);
        parameters.setShare_route(Boolean.FALSE);
        parameters.setRoute_name("Single Driver Route 10 Stops");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistance_unit(DistanceUnit.MI.toString());
        parameters.setDevice_type(DeviceType.WEB.toString());

        addresses.add(new Address(
                "151 Arbor Way Milledgeville GA 31061", Boolean.TRUE,
                33.132675170898,
                -83.244743347168,
                0
        ));
        addresses.add(new Address(
                "230 Arbor Way Milledgeville GA 31061",
                33.129695892334,
                -83.24577331543,
                0
        ));
        addresses.add(new Address(
                "148 Bass Rd NE Milledgeville GA 31061",
                33.143497,
                -83.224487,
                0
        ));
        addresses.add(new Address(
                "117 Bill Johnson Rd NE Milledgeville GA 31061",
                33.141784667969,
                -83.237518310547,
                0
        ));
        addresses.add(new Address(
                "119 Bill Johnson Rd NE Milledgeville GA 31061",
                33.141086578369,
                -83.238258361816,
                0
        ));
        addresses.add(new Address(
                "131 Bill Johnson Rd NE Milledgeville GA 31061",
                33.142036437988,
                -83.238845825195,
                0
        ));
        addresses.add(new Address(
                "138 Bill Johnson Rd NE Milledgeville GA 31061",
                33.14307,
                -83.239334,
                0
        ));
        addresses.add(new Address(
                "139 Bill Johnson Rd NE Milledgeville GA 31061",
                33.142734527588,
                -83.237442016602,
                0
        ));
        addresses.add(new Address(
                "145 Bill Johnson Rd NE Milledgeville GA 31061",
                33.143871307373,
                -83.237342834473,
                0
        ));
        addresses.add(new Address(
                "221 Blake Cir Milledgeville GA 31061",
                33.081462860107,
                -83.208511352539,
                0
        ));

        route4me.getOptimization().setData(data);
        Response response = route4me.runOptimization();
        DataObject responseObject = Base.GSONDeserializer.fromJson(response.getResponseBody(), DataObject.class);
        String jsonResponse = Base.GSONSerializer.toJson(responseObject);
        System.out.println(jsonResponse);
        System.out.println("Response Code:" + response.getResponseCode());
        System.out.println("Optimization Problem ID:" + responseObject.getOptimization_problem_id());
        System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
        if (responseObject.getAddresses() != null) {
            for (Address address : responseObject.getAddresses()) {
                System.out.println("Address:" + address.getAddress());
                System.out.println("Route ID:" + address.getRoute_id());
            }
        }
    }

}
