/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.examples;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.Order;
import com.route4me.sdk.services.orders.OrdersManager;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Constants.AlgorithmType;
import com.route4me.sdk.services.routing.Constants.DeviceType;
import com.route4me.sdk.services.routing.Constants.OptimizationState;
import com.route4me.sdk.services.routing.Constants.TravelMode;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.routing.RoutingManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Finds the orders with a custom data value with {@link OrdersManager#getOrdersByCustomField} and optimizes routes
 * from one depot for several drivers to them, with {@link RoutingManager#runOptimization}. Set
 * {@code ORDER_CUSTOM_FIELD_KEY} and {@code ORDER_CUSTOM_FIELD_VALUE} to a custom data key of your orders and the value
 * to match; the example stops when no order matches.
 */
public class SingleDepotMultipleDriverByOrderTag {

    public static void main(String[] args) {
        String key = System.getenv("ORDER_CUSTOM_FIELD_KEY");
        String value = System.getenv("ORDER_CUSTOM_FIELD_VALUE");
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            System.err.println("Set ORDER_CUSTOM_FIELD_KEY and ORDER_CUSTOM_FIELD_VALUE to a custom data key of your"
                    + " orders and the value to match; this example optimizes routes to the matching orders.");
            return;
        }
        try {
            String apiKey = System.getenv("R4M_API_KEY");
            RoutingManager manager = new RoutingManager(apiKey, true);
            OptimizationParameters optParameters = new OptimizationParameters();
            OrdersManager ordersManager = new OrdersManager(apiKey);
            Parameters parameters = new Parameters();
            parameters.setAlgorithmType(AlgorithmType.CVRP_TW_SD.getValue());
            parameters.setStoreRoute(Boolean.FALSE);
            parameters.setShareRoute(Boolean.FALSE);
            parameters.setRouteTime(0);
            parameters.setRouteName("Single Depot, Multiple Driver - By Orders Custom Data Key-Value");
            parameters.setDeviceType(DeviceType.WEB.toString());
            parameters.setTravelMode(TravelMode.DRIVING.toString());
            optParameters.setParameters(parameters);
            List<Address> addresses = new ArrayList<>();
            List<Order> orders = ordersManager.getOrdersByCustomField(key, value);
            if (orders == null || orders.isEmpty()) {
                System.out.println("No order has " + key + " = " + value + "; nothing to optimize.");
                return;
            }
            for (Order order:  orders) {
                Address address = new Address();
                address.setOrderId(order.getId().intValue());
                addresses.add(address);
            }
            optParameters.setAddresses(addresses);
            try {
                DataObject responseObject = manager.runOptimization(optParameters);
                System.out.println("Optimization Problem ID:" + responseObject.getOptimizationProblemId());
                System.out.println("State:" + (responseObject.getState() == null ? "not returned"
                        : OptimizationState.get(responseObject.getState().intValue())));
                if (responseObject.getAddresses() != null) {
                    for (Address address : responseObject.getAddresses()) {
                        System.out.println(address);
                    }
                }
            } catch (APIException e) {
                //handle exception
                e.printStackTrace();
            }
        } catch (APIException ex) {
            Logger.getLogger(SingleDepotMultipleDriverByOrderTag.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
