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
     * <p>The whole file is read into memory, and the call waits for it with this manager's read timeout
     * ({@link #getSocketTimeout()}, 30 seconds by default). A long period, or {@code show_cd} and {@code show_mmd},
     * can make the file large and slow to build: narrow {@code time_period} or the start and end dates, or export
     * one member, device or route at a time, and raise the timeout with {@link #setSocketTimeout(int)} for slow
     * exports.
     *
     * @param request the member, device or route, the period and the format of the file
     * @return the file, as text held in memory: CSV, JSON or XML, as {@code format} asks
     * @throws APIException if the request fails, times out or the server answers with an error status
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
