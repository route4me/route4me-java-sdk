package com.route4me.sdk.services.tracking;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import org.apache.http.client.utils.URIBuilder;

public class TrackingManager extends Manager {
    public static final String LAST_LOCATION_EP = "/api.v4/route.php";
    public static final String SET_LOCATION_EP = "/track/set.php";

    public TrackingManager(String apiKey) {
        super(apiKey);
    }

    public void setGPS(GPSPosition position) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(SET_LOCATION_EP);
        this.makeJSONRequest(RequestMethod.GET, builder, position, null);
    }

    public DataObject getLastLocation(String routeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(LAST_LOCATION_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("device_tracking_history", "1");
        return this.makeRequest(RequestMethod.GET, builder, "", DataObject.class);
    }
}
