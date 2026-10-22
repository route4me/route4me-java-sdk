package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.DataObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Records and reads GPS positions through the V4 API ({@code https://api.route4me.com}): positions of routes, the
 * location history of devices, the last positions of users, and device licenses.
 */
public class TrackingManager extends Manager {
    /** The path that reads a route with its tracking history, on {@code api.route4me.com}. */
    public static final String LAST_LOCATION_EP = "/api.v4/route.php";
    /** The path that records a GPS position, on {@code api.route4me.com}. */
    public static final String SET_LOCATION_EP = "/track/set.php";
    private static final String DEVICE_LOCATION_EP = "/api/track/get_device_location.php";
    private static final String USER_LOCATIONS_EP = "/api/track/view_user_locations.php";
    private static final String VERIFY_DEVICE_LICENSE_EP = "/api/device/verify_device_license.php";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public TrackingManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Records a GPS position of a route: {@code GET /track/set.php}, with the position as
     * query parameters and no body.
     *
     * @param position the position, with its route, member and device
     * @return the server's answer, with the status and the transaction ID
     * @throws APIException if the request fails
     */
    public SetGpsResponse setGPS(GPSPosition position) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(SET_LOCATION_EP);
        return this.makeJSONRequest(RequestMethod.GET, builder, position, SetGpsResponse.class);
    }

    /**
     * Records one GPS ping, or a batch of up to 100 pings: {@code POST https://api.route4me.com/track/set.php}, with
     * the request as the JSON body. Unlike {@link #setGPS(GPSPosition)}, which sends one position in the query, this
     * takes the pings of a batch in {@code items}.
     *
     * @param request the member, device and vehicle, and either one ping or the pings in {@code items}
     * @return the status: the transaction ID of a single ping, or the rejected items of a batch
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if the request is {@code null}; if it has no {@code items} and no {@code lat}
     *                                  and {@code lng}; or if {@code items} is empty, holds more than
     *                                  {@link TrackingPingsRequest#MAX_ITEMS} pings, or holds a ping without
     *                                  {@code lat} and {@code lng}
     */
    public TrackingPingsResponse setTrackingPings(TrackingPingsRequest request) throws APIException {
        checkPings(request);
        URIBuilder builder = Manager.defaultBuilder(SET_LOCATION_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), TrackingPingsResponse.class);
    }

    /** Checks the fields the track-set spec requires for one ping or a batch. */
    private static void checkPings(TrackingPingsRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("The request is required");
        }
        if (request.getItems() == null) {
            if (request.getLat() == null || request.getLng() == null) {
                throw new IllegalArgumentException("A single ping needs lat and lng");
            }
            return;
        }
        int count = request.getItems().size();
        if (count == 0 || count > TrackingPingsRequest.MAX_ITEMS) {
            throw new IllegalArgumentException("A batch needs 1 to " + TrackingPingsRequest.MAX_ITEMS
                    + " items, not " + count);
        }
        for (int i = 0; i < count; i++) {
            TrackingPing item = request.getItems().get(i);
            if (item == null || item.getLat() == null || item.getLng() == null) {
                throw new IllegalArgumentException("Item " + i + " needs lat and lng");
            }
        }
    }

    /**
     * Gets a route with its tracking history: {@code GET
     * https://api.route4me.com/api.v4/route.php?route_id=...&device_tracking_history=1}.
     *
     * @param routeId the route ID
     * @return the route, with its tracking history
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DataObject getLastLocation(String routeId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(LAST_LOCATION_EP);
        builder.setParameter("route_id", routeId);
        builder.setParameter("device_tracking_history", "1");
        return this.makeRequest(RequestMethod.GET, builder, "", DataObject.class);
    }

    /**
     * Gets the location history of the devices of a route or a member, for a period: {@code GET
     * https://api.route4me.com/api/track/get_device_location.php?format=json&...}. The answer is always asked for as
     * JSON.
     *
     * @param request the route or member, and the period: a named {@code time_period}, or {@code custom} with
     *                {@code start_date} and {@code end_date}
     * @return the positions, with their map-matching data
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DeviceLocationHistoryResponse getDeviceLocationHistory(DeviceLocationHistoryRequest request)
            throws APIException {
        URIBuilder builder = Manager.defaultBuilder(DEVICE_LOCATION_EP);
        builder.setParameter("format", "json");
        return this.makeJSONRequest(RequestMethod.GET, builder, request, DeviceLocationHistoryResponse.class);
    }

    /**
     * Gets the last known position of each user of the account: {@code GET
     * https://api.route4me.com/api/track/view_user_locations.php}, with {@code query} when it is given.
     *
     * @param query text to search the users by, such as an e-mail, a name or a member ID ({@code query}); {@code null}
     *              returns every user
     * @return the users with their last position
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public UserLocation[] getUserLocations(String query) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(USER_LOCATIONS_EP);
        if (query != null) {
            builder.setParameter("query", query);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, UserLocation[].class);
    }

    /**
     * Verifies the license of a device, or adds a record of the device: {@code POST
     * https://api.route4me.com/api/device/verify_device_license.php?device_id=...&device_type=...}, with
     * {@code device_id}, {@code device_type} and {@code format} as the JSON body, as the PHP SDK's
     * {@code Member::addDeviceRecord} sends them.
     *
     * @param deviceId the device ID ({@code device_id})
     * @param deviceType the device type, such as {@code iphone} or {@code android_phone} ({@code device_type})
     * @param format the format of the answer, such as {@code json} ({@code format})
     * @return the answer as text; it is plain text, not JSON, when the device ID is missing or invalid
     * @throws APIException if the request fails or the server answers with an error status
     */
    public String verifyDeviceLicense(String deviceId, String deviceType, String format) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(VERIFY_DEVICE_LICENSE_EP);
        if (deviceId != null) {
            builder.setParameter("device_id", deviceId);
        }
        if (deviceType != null) {
            builder.setParameter("device_type", deviceType);
        }
        String body = this.gson.toJson(new DeviceLicenseBody(deviceId, deviceType, format));
        return this.makeRequest(RequestMethod.POST, builder, body, String.class);
    }

    /** The body of {@link #verifyDeviceLicense}. */
    @Getter
    @AllArgsConstructor
    private static final class DeviceLicenseBody {
        /** The device ID ({@code device_id}). */
        @SerializedName("device_id")
        private final String deviceId;
        /** The device type ({@code device_type}). */
        @SerializedName("device_type")
        private final String deviceType;
        /** The format of the answer ({@code format}). */
        @SerializedName("format")
        private final String format;
    }
}
