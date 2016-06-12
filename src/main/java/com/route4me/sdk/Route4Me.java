package com.route4me.sdk;

import com.route4me.sdk.services.activities.ActivityManager;
import com.route4me.sdk.services.addressbook.AddressBookManager;
import com.route4me.sdk.services.routing.AddressesManager;
import com.route4me.sdk.services.zones.AvoidanceZoneManager;
import com.route4me.sdk.services.notes.NotesManager;
import com.route4me.sdk.managers.OptimizationManager;
import com.route4me.sdk.managers.RouteManager;
import com.route4me.sdk.managers.TrackingManager;
import com.route4me.sdk.services.users.UsersManager;

/**
 *
 * @author juan
 */
public class Route4Me {
    public static final String HOST = "https://www.route4me.com";
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
    private final ActivityManager activityManager;
    private final UsersManager userManager;
    private final NotesManager notesManager;

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
        this.notesManager = new NotesManager(apiKey);

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
     * @return the userManager
     */
    public UsersManager getUserManager() {
        return userManager;
    }

    /**
     * @return the notesManager
     */
    public NotesManager getNotesManager() {
        return notesManager;
    }

}
