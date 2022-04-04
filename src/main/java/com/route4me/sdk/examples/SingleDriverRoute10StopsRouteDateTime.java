// codebeat:disable[SIMILARITY]
package com.route4me.sdk.examples;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Constants.*;
import com.route4me.sdk.services.routing.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juan
 */
public class SingleDriverRoute10StopsRouteDateTime {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);
        OptimizationParameters optParameters = new OptimizationParameters();

        Parameters parameters = new Parameters();
        List<Address> addresses = new ArrayList<>();
        parameters.setAlgorithmType(AlgorithmType.CVRP_TW_SD.getValue());
        parameters.setStoreRoute(Boolean.FALSE);
        parameters.setShareRoute(Boolean.FALSE);
        parameters.setRouteName("Single Driver Route 10 Stops Custom - Route Date/Time");
        parameters.setOptimize(Optimize.DISTANCE.toString());
        parameters.setDistanceUnit(DistanceUnit.MI.toString());
        parameters.setDeviceType(DeviceType.WEB.toString());
        
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu'T'HH:mm:ss:SSSXXXXX");        
        
        String routeDateString = "09/12/2021T00:00:00:000-05:00";
        String routeTimeString = "01/01/1970T08:00:00:000-05:00";
        
        OffsetDateTime routeDateInstanceAtOffset = OffsetDateTime.parse(routeDateString, DATE_TIME_FORMATTER);
        OffsetDateTime routeTimeInstanceAtOffset = OffsetDateTime.parse(routeTimeString, DATE_TIME_FORMATTER);

        
        OffsetDateTime routeDateInstanceAtUTC = routeDateInstanceAtOffset.withOffsetSameInstant(ZoneOffset.UTC);
        OffsetDateTime routeTimeInstanceAtUTC = routeTimeInstanceAtOffset.withOffsetSameInstant(ZoneOffset.UTC);
        parameters.setRouteDate(routeDateInstanceAtUTC.toEpochSecond());
        parameters.setRouteTime(routeTimeInstanceAtUTC.toEpochSecond());
        optParameters.setParameters(parameters);

        addresses.add(new Address("1604 PARKRIDGE PKWY, Louisville, KY, 40214", true, 38.141598, -85.793846, 300, 47400, 48000));
        addresses.add(new Address("1407 MCCOY, Louisville, KY, 40215", 38.202496, -85.786514, 300, 48000, 48600));
        addresses.add(new Address("4805 BELLEVUE AVE, Louisville, KY, 40215", 38.178844, -85.774864, 300, 48600, 49200));
        addresses.add(new Address("730 CECIL AVENUE, Louisville, KY, 40211", 38.248684, -85.821121, 300, 49200, 49800));
        addresses.add(new Address("650 SOUTH 29TH ST UNIT 315, Louisville, KY, 40211", 38.251923, -85.800034, 300, 49800, 50400));
        addresses.add(new Address("4629 HILLSIDE DRIVE, Louisville, KY, 40216", 38.176067, -85.824638, 300, 50400, 51000));
        addresses.add(new Address("4738 BELLEVUE AVE, Louisville, KY, 40215", 38.179806, -85.775558, 300, 51000, 51600));
        addresses.add(new Address("318 SO. 39TH STREET, Louisville, KY, 40212", 38.259335, -85.815094, 300, 51600, 52200));
        addresses.add(new Address("1324 BLUEGRASS AVE, Louisville, KY, 40215", 38.179253, -85.785118, 300, 52200, 52800));
        addresses.add(new Address("7305 ROYAL WOODS DR, Louisville, KY, 40214", 38.162472, -85.792854, 300, 52800, 53400));
        addresses.add(new Address("1661 W HILL ST, Louisville, KY, 40210", 38.229584, -85.783966, 300, 53400, 54000));
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
// codebeat:enable[SIMILARITY]
