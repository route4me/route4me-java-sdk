package com.route4me.sdk;

import com.route4me.sdk.managers.AddressBookManager;
import com.route4me.sdk.managers.AddressesManager;
import com.route4me.sdk.managers.AvoidanceZoneManager;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.managers.TrackingManager;

/**
 *
 * @author juan
 */
public class Route4Me {

    public static final String CHARSET = "UTF-8";

    public static volatile String apiKey;
    
    private final AddressBookManager addressBookManager;
    private final AddressesManager addressesManager;
    private final AvoidanceZoneManager avoidanceZoneManager;
    private final OptimizationManager optimizationManager;
    private final RouteManager routeManager;
    private final TrackingManager trackingManager;
    
    
    public Route4Me(String apiKey) {
        Route4Me.apiKey = apiKey;
        this.optimizationManager = new OptimizationManager(apiKey);
        this.trackingManager = new TrackingManager(apiKey);
        this.routeManager = new RouteManager(apiKey);
        this.addressesManager = new AddressesManager(apiKey);
        this.addressBookManager = new AddressBookManager(apiKey);
        this.avoidanceZoneManager = new AvoidanceZoneManager(apiKey);
    }

    /**
     * @return the routeManager
     */
    public RouteManager getRouteManager() {
        return routeManager;
    }

    /**
     * @return the optimizationManager
     */
    public OptimizationManager getOptimizationManager() {
        return optimizationManager;
    }

    /**
     * @return the trackingManager
     */
    public TrackingManager getTrackingManager() {
        return trackingManager;
    }

    /**
     * @return the addressesManager
     */
    public AddressesManager getAddressesManager() {
        return addressesManager;
    }

    /**
     * @return the addressBookManager
     */
    public AddressBookManager getAddressBookManager() {
        return addressBookManager;
    }

    /**
     * @return the avoidanceZoneManager
     */
    public AvoidanceZoneManager getAvoidanceZoneManager() {
        return avoidanceZoneManager;
    }
}
