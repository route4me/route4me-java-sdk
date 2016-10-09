package com.route4me.sdk.services.tracking;

import com.google.gson.Gson;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import org.apache.http.client.utils.URIBuilder;

public class TrackingManager extends Manager {
    public static final String LAST_LOCATION_EP = "/api.v4/route.php";
    public static final String SET_LOCATION_EP = "/track/set.php";

    public TrackingManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public DataObject setGPS(GPSPosition position) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(LAST_LOCATION_EP);
        if (position.getFormat() != null) {
            builder.setParameter("format", position.getFormat().toString());
        }
        if (position.getRouteId() != null) {
            builder.setParameter("route_id", position.getRouteId());
        }
        if (position.getLatitude() != null) {
            builder.setParameter("lat", Double.toString(position.getLatitude()));
        }
        if (position.getLongitude() != null) {
            builder.setParameter("lng", Double.toString(position.getLongitude()));
        }
        if (position.getAltitude() != null) {
            builder.setParameter("altitude", Double.toString(position.getAltitude()));
        }
        if (position.getCourse() != null) {
            builder.setParameter("course", Integer.toString(position.getCourse()));
        }
        if (position.getAppVersion() != null) {
            builder.setParameter("app_version", position.getAppVersion());
        }
        if (position.getDeviceGUID() != null) {
            builder.setParameter("device_guid", position.getDeviceGUID());
        }
        if (position.getDeviceTimestamp() != null) {
            builder.setParameter("device_timestamp", position.getDeviceTimestamp());
        }
        if (position.getDeviceType() != null) {
            builder.setParameter("device_type", position.getDeviceType().toString());
        }
        if (position.getMemberId() != null) {
            builder.setParameter("member_id", position.getMemberId());
        }
        return this.makeRequest(RequestMethod.GET, builder, "", DataObject.class);
    }

    public DataObject getLastLocation(String routeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(LAST_LOCATION_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("device_tracking_history", "1");
        return this.makeRequest(RequestMethod.GET, builder, "", DataObject.class);
    }
}
