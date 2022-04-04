// codebeat:disable[ABC, SIMILARITY]
package com.route4me.sdk.examples;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants.*;
import com.route4me.sdk.services.routing.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juan
 */
public class SingleDepotMultipleDriverTimeWindow {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        parameters.setAlgorithmType(AlgorithmType.CVRP_TW_MD.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteTime(0);
        parameters.setRt(Boolean.TRUE);
        parameters.setRouteMaxDuration(86400);
        parameters.setVehicleCapacity("99");
        parameters.setVehicleMaxDistanceMi("99999");
        parameters.setRouteName("Multiple Depot, Multiple Driver, Time Window");
        parameters.setOptimize(Optimize.TIME.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        parameters.setTravelMode(TravelMode.DRIVING.toString());
        parameters.setParts(20);
        optParameters.setParameters(parameters);

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("455 S 4th St, Louisville, KY 40202", true, 38.251698, -85.757308, 300, 28800, 29400));
        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", 38.141598, -85.793846, 300, 29400, 30000));
        addresses.add(new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300, 30000, 30600));
        addresses.add(new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864, 300, 30600, 31200));
        addresses.add(new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300, 31200, 31800));
        addresses.add(new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300, 31800, 32400));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300, 32400, 33000));
        addresses.add(new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300, 33000, 33600));
        addresses.add(new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300, 33600, 34200));
        addresses.add(new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300, 34200, 34800));
        addresses.add(new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214", 38.162472, -85.792854, 300, 34800, 35400));
        addresses.add(new Address("1661 W HILL ST, Louisville, KY, 40210", 38.229584, -85.783966, 300, 35400, 36000));
        addresses.add(new Address("3222 KINGSWOOD WAY, Louisville, KY, 40216", 38.210606, -85.822594, 300, 36000, 36600));
        addresses.add(new Address("1922 PALATKA RD, Louisville, KY, 40214", 38.153767, -85.796783, 300, 36600, 37200));
        addresses.add(new Address("1314 SOUTH 26TH STREET, Louisville, KY, 40210", 38.235847, -85.796852, 300, 37200, 37800));
        addresses.add(new Address("2135 MCCLOSKEY AVENUE, Louisville, KY, 40210", 38.218662, -85.789032, 300, 37800, 38400));
        addresses.add(new Address("1409 PHYLLIS AVE, Louisville, KY, 40215", 38.206154, -85.781387, 300, 38400, 39000));
        addresses.add(new Address("4504 SUNFLOWER AVE, Louisville, KY, 40216", 38.187511, -85.839149, 300, 39000, 39600));
        addresses.add(new Address("2512 GREENWOOD AVE, Louisville, KY, 40210", 38.241405, -85.795059, 300, 39600, 40200));
        addresses.add(new Address("5500 WILKE FARM AVE, Louisville, KY, 40216", 38.166065, -85.863319, 300, 40200, 40800));
        addresses.add(new Address("3640 LENTZ AVE, Louisville, KY, 40215", 38.193283, -85.786201, 300, 40800, 41400));
        addresses.add(new Address("1020 BLUEGRASS AVE, Louisville, KY, 40215", 38.17952, -85.780037, 300, 41400, 42000));
        addresses.add(new Address("123 NORTH 40TH ST, Louisville, KY, 40212", 38.26498, -85.814156, 300, 42000, 42600));
        addresses.add(new Address("7315 ST ANDREWS WOODS CIRCLE UNIT 104, Louisville, KY, 40214", 38.151072, -85.802867, 300, 42600, 43200));
        addresses.add(new Address("3210 POPLAR VIEW DR, Louisville, KY, 40216", 38.182594, -85.849937, 300, 43200, 43800));
        addresses.add(new Address("4519 LOUANE WAY, Louisville, KY, 40216", 38.1754, -85.811447, 300, 43800, 44400));
        addresses.add(new Address("6812 MANSLICK RD, Louisville, KY, 40214", 38.161839, -85.798279, 300, 44400, 45000));
        addresses.add(new Address("1524 HUNTOON AVENUE, Louisville, KY, 40215", 38.172031, -85.788353, 300, 45000, 45600));
        addresses.add(new Address("1307 LARCHMONT AVE, Louisville, KY, 40215", 38.209663, -85.779816, 300, 45600, 46200));
        addresses.add(new Address("434 N 26TH STREET #2, Louisville, KY, 40212", 38.26844, -85.791962, 300, 46200, 46800));
        addresses.add(new Address("678 WESTLAWN ST, Louisville, KY, 40211", 38.250397, -85.80629, 300, 46800, 47400));
        addresses.add(new Address("2308 W BROADWAY, Louisville, KY, 40211", 38.248882, -85.790421, 300, 47400, 48000));
        addresses.add(new Address("2332 WOODLAND AVE, Louisville, KY, 40210", 38.233579, -85.794257, 300, 48000, 48600));
        addresses.add(new Address("1706 WEST ST. CATHERINE, Louisville, KY, 40210", 38.239697, -85.783928, 300, 48600, 49200));
        addresses.add(new Address("1699 WATHEN LN, Louisville, KY, 40216", 38.216465, -85.792397, 300, 49200, 49800));
        addresses.add(new Address("2416 SUNSHINE WAY, Louisville, KY, 40216", 38.186245, -85.831787, 300, 49800, 50400));
        addresses.add(new Address("6925 MANSLICK RD, Louisville, KY, 40214", 38.158466, -85.798355, 300, 50400, 51000));
        addresses.add(new Address("2707 7TH ST, Louisville, KY, 40215", 38.212438, -85.785082, 300, 51000, 51600));
        addresses.add(new Address("2014 KENDALL LN, Louisville, KY, 40216", 38.179394, -85.826668, 300, 51600, 52200));
        addresses.add(new Address("612 N 39TH ST, Louisville, KY, 40212", 38.273354, -85.812012, 300, 52200, 52800));
        addresses.add(new Address("2215 ROWAN ST, Louisville, KY, 40212", 38.261703, -85.786781, 300, 52800, 53400));
        addresses.add(new Address("1826 W. KENTUCKY ST, Louisville, KY, 40210", 38.241611, -85.78653, 300, 53400, 54000));
        addresses.add(new Address("1810 GREGG AVE, Louisville, KY, 40210", 38.224716, -85.796211, 300, 54000, 54600));
        addresses.add(new Address("4103 BURRRELL DRIVE, Louisville, KY, 40216", 38.191753, -85.825836, 300, 54600, 55200));
        addresses.add(new Address("359 SOUTHWESTERN PKWY, Louisville, KY, 40212", 38.259903, -85.823463, 300, 55200, 55800));
        addresses.add(new Address("2407 W CHESTNUT ST, Louisville, KY, 40211", 38.252781, -85.792109, 300, 55800, 56400));
        addresses.add(new Address("225 S 22ND ST, Louisville, KY, 40212", 38.257616, -85.786658, 300, 56400, 57000));
        addresses.add(new Address("1404 MCCOY AVE, Louisville, KY, 40215", 38.202122, -85.786072, 300, 57000, 57600));
        addresses.add(new Address("117 FOUNT LANDING CT, Louisville, KY, 40212", 38.270061, -85.799438, 300, 57600, 58200));
        addresses.add(new Address("5504 SHOREWOOD DRIVE, Louisville, KY, 40214", 38.145851, -85.7798, 300, 58200, 58800));
        optParameters.setAddresses(addresses);

        try {
            DataObject responseObject = manager.runOptimization(optParameters);
            System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
            System.out.println("State:" + OptimizationState.get(responseObject.getState().intValue()));
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
// codebeat:enable[ABC, SIMILARITY]
