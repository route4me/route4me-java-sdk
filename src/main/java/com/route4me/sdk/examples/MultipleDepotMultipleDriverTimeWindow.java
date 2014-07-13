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
public class MultipleDepotMultipleDriverTimeWindow {

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
        parameters.setAlgorithm_type(AlgorithmType.CVRP_TW_MD.getValue());
        parameters.setStore_route(Boolean.FALSE);
        parameters.setShare_route(Boolean.FALSE);
        parameters.setRoute_time(0);
        parameters.setRt(Boolean.TRUE);
        parameters.setRoute_max_duration(86400);
        parameters.setVehicle_capacity("99");
        parameters.setVehicle_max_distance_mi("99999");
        parameters.setRoute_name("Multiple Depot, Multiple Driver, Time Window");
        parameters.setOptimize(Optimize.TIME.toString());
        parameters.setDistance_unit(DistanceUnit.MI.toString());
        parameters.setDevice_type(DeviceType.WEB.toString());
        parameters.setTravel_mode(TravelMode.DRIVING.toString());
        parameters.setMetric(Metric.ROUTE4ME_METRIC_GEODESIC.getValue());
        addresses.add(new Address(
                "455 S 4th St, Louisville, KY 40202", Boolean.TRUE,
                38.251698,
                -85.757308,
                300,
                28800,
                30477
        ));
        addresses.add(new Address(
                "1604 PARKRIDGE PKWY, Louisville, KY, 40214",
                38.141598,
                -85.793846,
                300,
                30477,
                33406
        ));
        addresses.add(new Address(
                "1407 MCCOY, Louisville, KY, 40215",
                38.202496,
                -85.786514,
                300,
                33406,
                36228
        ));
        addresses.add(new Address(
                "4805 BELLEVUE AVE, Louisville, KY, 40215",
                38.178844,
                -85.774864,
                300,
                36228,
                37518
        ));
        addresses.add(new Address(
                "730 CECIL AVENUE, Louisville, KY, 40211",
                38.248684,
                -85.821121,
                300,
                37518,
                39550
        ));
        addresses.add(new Address(
                "650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211",
                38.251923,
                -85.800034,
                300,
                39550,
                41348
        ));
        addresses.add(new Address(
                "4629 HILLSIDE DRIVE, Louisville, KY, 40216",
                38.176067,
                -85.824638,
                300,
                41348,
                42261
        ));
        addresses.add(new Address(
                "4738 BELLEVUE AVE, Louisville, KY, 40215",
                38.179806,
                -85.775558,
                300,
                42261,
                45195
        ));
        addresses.add(new Address(
                "318 SO. 39TH STREET, Louisville, KY, 40212",
                38.259335,
                -85.815094,
                300,
                45195,
                46549
        ));
        addresses.add(new Address(
                "1324 BLUEGRASS AVE, Louisville, KY, 40215",
                38.179253,
                -85.785118,
                300,
                46549,
                47353
        ));
        addresses.add(new Address(
                "7305 ROYAL WOODS DR, Louisville, KY, 40214",
                38.162472,
                -85.792854,
                300,
                47353,
                50924
        ));
        addresses.add(new Address(
                "1661 W HILL ST, Louisville, KY, 40210",
                38.229584,
                -85.783966,
                300,
                50924,
                51392
        ));
        addresses.add(new Address(
                "3222 KINGSWOOD WAY, Louisville, KY, 40216",
                38.210606,
                -85.822594,
                300,
                51392,
                52451
        ));
        addresses.add(new Address(
                "1922 PALATKA RD, Louisville, KY, 40214",
                38.153767,
                -85.796783,
                300,
                52451,
                55631
        ));
        addresses.add(new Address(
                "1314 SOUTH 26TH STREET, Louisville, KY, 40210",
                38.235847,
                -85.796852,
                300,
                55631,
                58516
        ));
        addresses.add(new Address(
                "2135 MCCLOSKEY AVENUE, Louisville, KY, 40210",
                38.218662,
                -85.789032,
                300,
                58516,
                61080
        ));
        addresses.add(new Address(
                "1409 PHYLLIS AVE, Louisville, KY, 40215",
                38.206154,
                -85.781387,
                300,
                61080,
                61104
        ));
        addresses.add(new Address(
                "4504 SUNFLOWER AVE, Louisville, KY, 40216",
                38.187511,
                -85.839149,
                300,
                61104,
                62061
        ));
        addresses.add(new Address(
                "2512 GREENWOOD AVE, Louisville, KY, 40210",
                38.241405,
                -85.795059,
                300,
                62061,
                65012
        ));
        addresses.add(new Address(
                "5500 WILKE FARM AVE, Louisville, KY, 40216",
                38.166065,
                -85.863319,
                300,
                65012,
                67541
        ));
        addresses.add(new Address(
                "3640 LENTZ AVE, Louisville, KY, 40215",
                38.193283,
                -85.786201,
                300,
                67541,
                69120
        ));
        addresses.add(new Address(
                "1020 BLUEGRASS AVE, Louisville, KY, 40215",
                38.17952,
                -85.780037,
                300,
                69120,
                70572
        ));
        addresses.add(new Address(
                "123 NORTH 40TH ST, Louisville, KY, 40212",
                38.26498,
                -85.814156,
                300,
                70572,
                73177
        ));
        addresses.add(new Address(
                "7315 ST ANDREWS WOODS CIRCLE UNIT 104, Louisville, KY, 40214",
                38.151072,
                -85.802867,
                300,
                73177,
                75231
        ));
        addresses.add(new Address(
                "3210 POPLAR VIEW DR, Louisville, KY, 40216",
                38.182594,
                -85.849937,
                300,
                75231,
                77663
        ));
        addresses.add(new Address(
                "4519 LOUANE WAY, Louisville, KY, 40216",
                38.1754,
                -85.811447,
                300,
                77663,
                79796
        ));
        addresses.add(new Address(
                "6812 MANSLICK RD, Louisville, KY, 40214",
                38.161839,
                -85.798279,
                300,
                79796,
                80813
        ));
        addresses.add(new Address(
                "1524 HUNTOON AVENUE, Louisville, KY, 40215",
                38.172031,
                -85.788353,
                300,
                80813,
                83956
        ));
        addresses.add(new Address(
                "1307 LARCHMONT AVE, Louisville, KY, 40215",
                38.209663,
                -85.779816,
                300,
                83956,
                84365
        ));
        addresses.add(new Address(
                "434 N 26TH STREET #2, Louisville, KY, 40212",
                38.26844,
                -85.791962,
                300,
                84365,
                84367
        ));
        addresses.add(new Address(
                "678 WESTLAWN ST, Louisville, KY, 40211",
                38.250397,
                -85.80629,
                300,
                84367,
                86362
        ));
        addresses.add(new Address(
                "2308 W BROADWAY, Louisville, KY, 40211",
                38.248882,
                -85.790421,
                300,
                86362,
                88703
        ));
        addresses.add(new Address(
                "2332 WOODLAND AVE, Louisville, KY, 40210",
                38.233579,
                -85.794257,
                300,
                88703,
                89320
        ));
        addresses.add(new Address(
                "1706 WEST ST. CATHERINE, Louisville, KY, 40210",
                38.239697,
                -85.783928,
                300,
                89320,
                90054
        ));
        addresses.add(new Address(
                "1699 WATHEN LN, Louisville, KY, 40216",
                38.216465,
                -85.792397,
                300,
                90054,
                90150
        ));
        addresses.add(new Address(
                "2416 SUNSHINE WAY, Louisville, KY, 40216",
                38.186245,
                -85.831787,
                300,
                90150,
                91915
        ));
        addresses.add(new Address(
                "6925 MANSLICK RD, Louisville, KY, 40214",
                38.158466,
                -85.798355,
                300,
                91915,
                93407
        ));
        addresses.add(new Address(
                "2707 7TH ST, Louisville, KY, 40215",
                38.212438,
                -85.785082,
                300,
                93407,
                95992
        ));
        addresses.add(new Address(
                "2014 KENDALL LN, Louisville, KY, 40216",
                38.179394,
                -85.826668,
                300,
                95992,
                99307
        ));
        addresses.add(new Address(
                "612 N 39TH ST, Louisville, KY, 40212",
                38.273354,
                -85.812012,
                300,
                99307,
                102906
        ));
        addresses.add(new Address(
                "2215 ROWAN ST, Louisville, KY, 40212",
                38.261703,
                -85.786781,
                300,
                102906,
                106021
        ));
        addresses.add(new Address(
                "1826 W. KENTUCKY ST, Louisville, KY, 40210",
                38.241611,
                -85.78653,
                300,
                106021,
                107276
        ));
        addresses.add(new Address(
                "1810 GREGG AVE, Louisville, KY, 40210",
                38.224716,
                -85.796211,
                300,
                107276,
                107948
        ));
        addresses.add(new Address(
                "4103 BURRRELL DRIVE, Louisville, KY, 40216",
                38.191753,
                -85.825836,
                300,
                107948,
                108414
        ));
        addresses.add(new Address(
                "359 SOUTHWESTERN PKWY, Louisville, KY, 40212",
                38.259903,
                -85.823463,
                300,
                108414,
                108685
        ));
        addresses.add(new Address(
                "2407 W CHESTNUT ST, Louisville, KY, 40211",
                38.252781,
                -85.792109,
                300,
                108685,
                110109
        ));
        addresses.add(new Address(
                "225 S 22ND ST, Louisville, KY, 40212",
                38.257616,
                -85.786658,
                300,
                110109,
                111375
        ));
        addresses.add(new Address(
                "1404 MCCOY AVE, Louisville, KY, 40215",
                38.202122,
                -85.786072,
                300,
                111375,
                112120
        ));
        addresses.add(new Address(
                "117 FOUNT LANDING CT, Louisville, KY, 40212",
                38.270061,
                -85.799438,
                300,
                112120,
                114095
        ));
        addresses.add(new Address(
                "5504 SHOREWOOD DRIVE, Louisville, KY, 40214",
                38.145851,
                -85.7798,
                300,
                114095,
                115743
        ));
        addresses.add(new Address(
                "1406 CENTRAL AVE, Louisville, KY, 40208",
                38.211025,
                -85.780251,
                300,
                115743,
                117716
        ));
        addresses.add(new Address(
                "901 W WHITNEY AVE, Louisville, KY, 40215",
                38.194115,
                -85.77494,
                300,
                117716,
                119078
        ));
        addresses.add(new Address(
                "2109 SCHAFFNER AVE, Louisville, KY, 40210",
                38.219699,
                -85.779363,
                300,
                119078,
                121147
        ));
        addresses.add(new Address(
                "2906 DIXIE HWY, Louisville, KY, 40216",
                38.209278,
                -85.798653,
                300,
                121147,
                124281
        ));
        addresses.add(new Address(
                "814 WWHITNEY AVE, Louisville, KY, 40215",
                38.193596,
                -85.773521,
                300,
                124281,
                124675
        ));
        addresses.add(new Address(
                "1610 ALGONQUIN PWKY, Louisville, KY, 40210",
                38.222153,
                -85.784187,
                300,
                124675,
                127148
        ));
        addresses.add(new Address(
                "3524 WHEELER AVE, Louisville, KY, 40215",
                38.195293,
                -85.788643,
                300,
                127148,
                130667
        ));
        addresses.add(new Address(
                "5009 NEW CUT RD, Louisville, KY, 40214",
                38.165905,
                -85.779701,
                300,
                130667,
                131980
        ));
        addresses.add(new Address(
                "3122 ELLIOTT AVE, Louisville, KY, 40211",
                38.251213,
                -85.804199,
                300,
                131980,
                134402
        ));
        addresses.add(new Address(
                "911 GAGEL AVE, Louisville, KY, 40216",
                38.173512,
                -85.807854,
                300,
                134402,
                136787
        ));
        addresses.add(new Address(
                "4020 GARLAND AVE #lOOA, Louisville, KY, 40211",
                38.246181,
                -85.818901,
                300,
                136787,
                138073
        ));
        addresses.add(new Address(
                "5231 MT HOLYOKE DR, Louisville, KY, 40216",
                38.169369,
                -85.85704,
                300,
                138073,
                141407
        ));
        addresses.add(new Address(
                "1339 28TH S #2, Louisville, KY, 40211",
                38.235275,
                -85.800156,
                300,
                141407,
                143561
        ));
        addresses.add(new Address(
                "836 S 36TH ST, Louisville, KY, 40211",
                38.24651,
                -85.811234,
                300,
                143561,
                145941
        ));
        addresses.add(new Address(
                "2132 DUNCAN STREET, Louisville, KY, 40212",
                38.262135,
                -85.785172,
                300,
                145941,
                148296
        ));
        addresses.add(new Address(
                "3529 WHEELER AVE, Louisville, KY, 40215",
                38.195057,
                -85.787949,
                300,
                148296,
                150177
        ));
        addresses.add(new Address(
                "2829 DE MEL #11, Louisville, KY, 40214",
                38.171662,
                -85.807271,
                300,
                150177,
                150981
        ));
        addresses.add(new Address(
                "1325 EARL AVENUE, Louisville, KY, 40215",
                38.204556,
                -85.781555,
                300,
                150981,
                151854
        ));
        addresses.add(new Address(
                "3632 MANSLICK RD #10, Louisville, KY, 40215",
                38.193542,
                -85.801147,
                300,
                151854,
                152613
        ));
        addresses.add(new Address(
                "637 S 41ST ST, Louisville, KY, 40211",
                38.253632,
                -85.81897,
                300,
                152613,
                156131
        ));
        addresses.add(new Address(
                "3420 VIRGINIA AVENUE, Louisville, KY, 40211",
                38.238693,
                -85.811386,
                300,
                156131,
                156212
        ));
        addresses.add(new Address(
                "3501 MALIBU CT APT 6, Louisville, KY, 40216",
                38.166481,
                -85.825928,
                300,
                156212,
                158655
        ));
        addresses.add(new Address(
                "4912 DIXIE HWY, Louisville, KY, 40216",
                38.170728,
                -85.826817,
                300,
                158655,
                159145
        ));
        addresses.add(new Address(
                "7720 DINGLEDELL RD, Louisville, KY, 40214",
                38.162472,
                -85.792854,
                300,
                159145,
                161831
        ));
        addresses.add(new Address(
                "2123 RATCLIFFE AVE, Louisville, KY, 40210",
                38.21978,
                -85.797615,
                300,
                161831,
                163705
        ));
        addresses.add(new Address(
                "1321 OAKWOOD AVE, Louisville, KY, 40215",
                38.17704,
                -85.783829,
                300,
                163705,
                164953
        ));
        addresses.add(new Address(
                "2223 WEST KENTUCKY STREET, Louisville, KY, 40210",
                38.242516,
                -85.790695,
                300,
                164953,
                166189
        ));
        addresses.add(new Address(
                "8025 GLIMMER WAY #3308, Louisville, KY, 40214",
                38.131981,
                -85.77935,
                300,
                166189,
                166640
        ));
        addresses.add(new Address(
                "1155 S 28TH ST, Louisville, KY, 40211",
                38.238621,
                -85.799911,
                300,
                166640,
                168147
        ));
        addresses.add(new Address(
                "840 IROQUOIS AVE, Louisville, KY, 40214",
                38.166355,
                -85.779396,
                300,
                168147,
                170385
        ));
        addresses.add(new Address(
                "5573 BRUCE AVE, Louisville, KY, 40214",
                38.145222,
                -85.779205,
                300,
                170385,
                171096
        ));
        addresses.add(new Address(
                "1727 GALLAGHER, Louisville, KY, 40210",
                38.239334,
                -85.784882,
                300,
                171096,
                171951
        ));
        addresses.add(new Address(
                "1309 CATALPA ST APT 204, Louisville, KY, 40211",
                38.236524,
                -85.801619,
                300,
                171951,
                172193
        ));
        addresses.add(new Address(
                "1330 ALGONQUIN PKWY, Louisville, KY, 40208",
                38.219846,
                -85.777344,
                300,
                172193,
                175337
        ));
        addresses.add(new Address(
                "823 SUTCLIFFE, Louisville, KY, 40211",
                38.246956,
                -85.811569,
                300,
                175337,
                176867
        ));
        addresses.add(new Address(
                "4405 CHURCHMAN AVENUE #2, Louisville, KY, 40215",
                38.177768,
                -85.792545,
                300,
                176867,
                178051
        ));
        addresses.add(new Address(
                "3211 DUMESNIL ST #1, Louisville, KY, 40211",
                38.237789,
                -85.807968,
                300,
                178051,
                178083
        ));
        addresses.add(new Address(
                "3904 WEWOKA AVE, Louisville, KY, 40212",
                38.270367,
                -85.813118,
                300,
                178083,
                181543
        ));
        addresses.add(new Address(
                "660 SO. 42ND STREET, Louisville, KY, 40211",
                38.252865,
                -85.822624,
                300,
                181543,
                184193
        ));
        addresses.add(new Address(
                "3619  LENTZ  AVE, Louisville, KY, 40215",
                38.193249,
                -85.785492,
                300,
                184193,
                185853
        ));
        addresses.add(new Address(
                "4305  STOLTZ  CT, Louisville, KY, 40215",
                38.178707,
                -85.787292,
                300,
                185853,
                187252
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
