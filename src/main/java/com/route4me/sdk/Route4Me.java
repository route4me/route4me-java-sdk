package com.route4me.sdk;

import com.route4me.sdk.managers.ActivityManager;
import com.route4me.sdk.managers.AddressBookManager;
import com.route4me.sdk.managers.AddressesManager;
import com.route4me.sdk.managers.AvoidanceZoneManager;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.managers.TrackingManager;
import com.route4me.sdk.managers.UsersManager;

/**
 *
 * @author juan
 */
public class Route4Me {

    public static final String CHARSET = "UTF-8";

    private static volatile String apiKey;

    /**
     * @return the apiKey
     */
    public static String getApiKey() {
        return apiKey;
    }

    /**
     * @param aApiKey the apiKey to set
     */
    public static void setApiKey(String aApiKey) {
        apiKey = aApiKey;
    }
    
    private final AddressBookManager addressBookManager;
    private final AddressesManager addressesManager;
    private final AvoidanceZoneManager avoidanceZoneManager;
    private final OptimizationManager optimizationManager;
    private final RouteManager routeManager;
    private final TrackingManager trackingManager;
    private ActivityManager activityManager;
    private UsersManager userManager;
    
    
    public Route4Me(String apiKey) {
        Route4Me.apiKey = apiKey;
        this.optimizationManager = new OptimizationManager(apiKey);
        this.trackingManager = new TrackingManager(apiKey);
        this.routeManager = new RouteManager(apiKey);
        this.addressesManager = new AddressesManager(apiKey);
        this.addressBookManager = new AddressBookManager(apiKey);
        this.avoidanceZoneManager = new AvoidanceZoneManager(apiKey);
        this.activityManager = new ActivityManager(apiKey);
        this.userManager = new UsersManager(apiKey);
        
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

    /**
     * @return the activityManager
     */
    public ActivityManager getActivityManager() {
        return activityManager;
    }

    /**
     * @param activityManager the activityManager to set
     */
    public void setActivityManager(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    /**
     * @return the userManager
     */
    public UsersManager getUserManager() {
        return userManager;
    }

    /**
     * @param userManager the userManager to set
     */
    public void setUserManager(UsersManager userManager) {
        this.userManager = userManager;
    }
}
