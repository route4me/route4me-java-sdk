package com.route4me.sdk.services.trackinghistory;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.client.utils.URIBuilder;

/**
 * Exports the GPS tracking history of members, devices and routes through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0/tracking-history}).
 */
public class TrackingHistoryManager extends Manager {

    private static final String EXPORT_EP = "/modules/api/v5.0/tracking-history/export";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public TrackingHistoryManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public TrackingHistoryManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    /**
     * Exports the tracking history as a file: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/tracking-history/export}, with the request as the JSON body.
     *
     * @param request the member, device or route, the period and the format of the file
     * @return the file, as text: CSV, JSON or XML, as {@code format} asks
     * @throws APIException if the request fails or the server answers with an error status
     */
    public String exportTrackingHistory(TrackingHistoryExportRequest request) throws APIException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(EXPORT_EP);
        String body = this.gson.toJson(request == null ? new TrackingHistoryExportRequest() : request);
        return this.makeRequest(RequestMethod.POST, builder, body, String.class);
    }
}
