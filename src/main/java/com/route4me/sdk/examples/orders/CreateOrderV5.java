package com.route4me.sdk.examples.orders;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.orders.GeoPoint;
import com.route4me.sdk.services.orders.OrderManagerV5;
import com.route4me.sdk.services.orders.OrderV5;

public class CreateOrderV5 {
    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        OrderManagerV5 manager = new OrderManagerV5(apiKey);
        try {
            OrderV5 order = new OrderV5();
            order.setAddress1("123 Main St, New York, NY");
            order.setAddressAlias("Test Order");
            order.setFirstName("John");
            order.setLastName("Doe");
            GeoPoint geo = new GeoPoint();
            geo.setLat(40.7128);
            geo.setLng(-74.0060);
            order.setAddressGeo(geo);
            OrderV5 result = manager.createOrder(order);
            System.out.println("Created order: " + result.getOrderUuid());
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}
