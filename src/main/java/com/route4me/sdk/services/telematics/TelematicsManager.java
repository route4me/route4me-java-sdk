/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import static com.route4me.sdk.queryconverter.QueryConverter.convertObjectToParameters;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages telematics integrations: the member registration and the vendor connections through the V4 API
 * ({@code https://api.route4me.com/api.v4/telematics}), and the vendor catalog through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0/telematics/vendors}).
 * The methods of the retired {@code telematics.route4me.com} host are deprecated: they throw an
 * {@link APIException} without sending a request, since the API key goes only to {@code api.route4me.com} and
 * {@code wh.route4me.com}.
 */
public class TelematicsManager extends Manager {

    /**
     * The vendors path on the retired {@code telematics.route4me.com} host.
     *
     * @deprecated the host is retired; the V5 vendor methods use {@link #TELEMATICS_VENDORS_V5}
     *             and {@link #TELEMATICS_VENDORS_ALL_V5}.
     */
    @Deprecated
    public static final String TELEMATICS_VENDORS_V4 = "/api/vendors.php";
    /** The path that registers a member for telematics, on {@code api.route4me.com}. */
    public static final String TELEMATICS_REGISTER_V4 = "/api.v4/telematics/register.php";
    /** The path of the telematics connections, on {@code api.route4me.com}. */
    public static final String TELEMATICS_CONNECTIONS_V4 = "/api.v4/telematics/connections.php";
    /** The path of the telematics vendors of a member, on {@code api.route4me.com}. */
    public static final String TELEMATICS_VENDORS_INFO_V4 = "/api.v4/telematics/vendors.php";
    /** One vendor on the V5 API: {@code GET ...?vendor_id=...}. */
    public static final String TELEMATICS_VENDORS_V5 = "/modules/api/v5.0/telematics/vendors";
    /** The vendor catalog on the V5 API. */
    public static final String TELEMATICS_VENDORS_ALL_V5 = "/modules/api/v5.0/telematics/vendors/all";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token to the V5 API and as the {@code api_key} query
     *     parameter to the V4 API
     */
    public TelematicsManager(String apiKey) {
        super(apiKey);
    }

    /**
     * The message of the {@link APIException} that the methods of the retired
     * {@code telematics.route4me.com} host throw.
     */
    static final String RETIRED_HOST_MESSAGE = "telematics.route4me.com is retired, so no request was sent: use the"
            + " V5 vendor methods getTelematicsVendorsV5(TelematicsVendorsV5Request) and getTelematicsVendorV5(long)";

    /**
     * Returns the exception that the methods of the retired {@code telematics.route4me.com} host throw instead of
     * sending a request, with the API key, to a host that is no longer Route4Me's API.
     *
     * @return the exception to throw
     */
    private static APIException retiredHost() {
        return new APIException(RETIRED_HOST_MESSAGE);
    }

    /**
     * Registers a member for telematics: {@code GET
     * https://api.route4me.com/api.v4/telematics/register.php?member_id=...}.
     *
     * @param memberID the member ID
     * @return the registration, with the telematics API token of the member
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsMemberRegister registerMember(Integer memberID) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_REGISTER_V4);
        builder.setParameter("member_id", memberID.toString());
        return makeRequest(RequestMethod.GET, builder, "", TelematicsMemberRegister.class);
    }

    /**
     * Lists the telematics connections of a member:
     * {@code GET /api.v4/telematics/connections.php?api_token=...}.
     *
     * @param apiToken the member's telematics API token
     * @return the connections
     * @throws APIException if the request fails
     */
    public List<TelematicsConnection> getTelematicsConnections(String apiToken) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.GET, builder, "", new TypeToken<ArrayList<TelematicsConnection>>() {
        }.getType());
    }

    /**
     * Lists the telematics vendors on {@code telematics.route4me.com}.
     *
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo getTelematicsVendorsInfo() throws APIException {
        throw retiredHost();
    }

    /**
     * Gets one telematics vendor on {@code telematics.route4me.com}.
     *
     * @param vendorID the vendor ID
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use {@link #getTelematicsVendorV5(long)}.
     */
    @Deprecated
    public TelematicsVendorsInfo getTelematicsVendorInfo(String vendorID) throws APIException {
        throw retiredHost();
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a country code.
     *
     * @param country a country code
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByCountry(String country) throws APIException {
        throw retiredHost();
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a country code, one page
     * at a time.
     *
     * @param country a country code
     * @param page    the page number, or {@code null}
     * @param perPage the page size, or {@code null}
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByCountry(String country, String page, String perPage)
            throws APIException {
        throw retiredHost();
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a vendor size.
     *
     * @param size a vendor size
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorBySize(String size) throws APIException {
        throw retiredHost();
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a vendor size, one page
     * at a time.
     *
     * @param size a vendor size
     * @param page    the page number, or {@code null}
     * @param perPage the page size, or {@code null}
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorBySize(String size, String page, String perPage) throws APIException {
        throw retiredHost();
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a keyword.
     *
     * @param keyWord a keyword
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByKeyWord(String keyWord) throws APIException {
        throw retiredHost();
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a keyword, one page
     * at a time.
     *
     * @param keyWord a keyword
     * @param page    the page number, or {@code null}
     * @param perPage the page size, or {@code null}
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByKeyWord(String keyWord, String page, String perPage)
            throws APIException {
        throw retiredHost();
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a feature.
     *
     * @param feature a feature
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByFeature(String feature) throws APIException {
        throw retiredHost();
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a feature, one page
     * at a time.
     *
     * @param feature a feature
     * @param page    the page number, or {@code null}
     * @param perPage the page size, or {@code null}
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByFeature(String feature, String page, String perPage)
            throws APIException {
        throw retiredHost();
    }

    /**
     * Lists the telematics vendors available to a member: {@code GET
     * https://api.route4me.com/api.v4/telematics/vendors.php?api_token=...}.
     *
     * @param apiToken the telematics API token of the member
     * @return the vendors
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsVendor[] getTelematicsVendor(String apiToken) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_VENDORS_INFO_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendor[].class);
    }

    /**
     * Connects a member to a telematics vendor: {@code POST
     * https://api.route4me.com/api.v4/telematics/connections.php?api_token=...}, with
     * the connection as a form.
     *
     * @param apiToken the telematics API token of the member
     * @param credentials the vendor, the credentials and the options of the connection
     * @return the created connection
     * @throws IllegalAccessException if the connection cannot be read for the form
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsConnection createTelematicsConnection(String apiToken, TelematicsConnection credentials)
            throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.POST, builder, convertObjectToParameters(credentials),
                TelematicsConnection.class);

    }

    /**
     * Updates a telematics connection:
     * {@code PUT https://api.route4me.com/api.v4/telematics/connections.php?api_token=...&connection_token=...}, with
     * the connection as a
     * form.
     *
     * @param apiToken the telematics API token of the member
     * @param connectionToken the token of the connection
     * @param credentials the new values
     * @return the updated connection
     * @throws IllegalAccessException if the connection cannot be read for the form
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsConnection updateTelematicsConnection(String apiToken, String connectionToken,
            TelematicsConnection credentials) throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.PUT, builder, convertObjectToParameters(credentials),
                TelematicsConnection.class);

    }

    /**
     * Deletes a telematics connection:
     * {@code DELETE https://api.route4me.com/api.v4/telematics/connections.php?api_token=...&connection_token=...}.
     *
     * @param apiToken the telematics API token of the member
     * @param connectionToken the token of the connection
     * @return the deleted connection
     * @throws IllegalAccessException never; declared for compatibility
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsConnection deleteTelematicsConnection(String apiToken, String connectionToken)
            throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.DELETE, builder, "", TelematicsConnection.class);

    }

    /**
     * Gets one telematics connection:
     * {@code GET https://api.route4me.com/api.v4/telematics/connections.php?api_token=...&connection_token=...}.
     *
     * @param apiToken the telematics API token of the member
     * @param connectionToken the token of the connection
     * @return the connection
     * @throws IllegalAccessException never; declared for compatibility
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsConnection getTelematicsConnection(String apiToken, String connectionToken)
            throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.GET, builder, "", TelematicsConnection.class);

    }

    /**
     * Lists the telematics vendor catalog:
     * {@code GET https://wh.route4me.com/modules/api/v5.0/telematics/vendors/all}.
     *
     * @return the vendors, under {@code data}
     * @throws APIException if the request fails
     */
    public TelematicsVendorsV5Response getTelematicsVendorsV5() throws APIException {
        return this.getTelematicsVendorsV5(null);
    }

    /**
     * Lists the telematics vendors that match some filters:
     * {@code GET https://wh.route4me.com/modules/api/v5.0/telematics/vendors/all}.
     *
     * <p>This replaces the deprecated {@link #getTelematicsVendorsInfo()} and the
     * {@code searchVendorBy...} methods, whose host is retired.
     *
     * @param filters the filters, or {@code null} for the whole catalog; booleans are sent
     *                as {@code 1} or {@code 0}
     * @return the matching vendors, under {@code data}
     * @throws APIException if the request fails
     */
    public TelematicsVendorsV5Response getTelematicsVendorsV5(TelematicsVendorsV5Request filters) throws APIException {
        URIBuilder builder = v5Builder(TELEMATICS_VENDORS_ALL_V5);
        if (filters != null) {
            setIfPresent(builder, "search", filters.getSearch());
            setIfPresent(builder, "country", filters.getCountry());
            setIfPresent(builder, "feature", filters.getFeature());
            builder.addParameters(QueryConverter.toBracketedParameters("features", filters.getFeatures()));
            setIfPresent(builder, "size", filters.getSize());
            setIfPresent(builder, "is_integrated", flag(filters.getIsIntegrated()));
            setIfPresent(builder, "display_hidden", flag(filters.getDisplayHidden()));
            setIfPresent(builder, "page", filters.getPage());
            setIfPresent(builder, "per_page", filters.getPerPage());
        }
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendorsV5Response.class);
    }

    /**
     * Gets one telematics vendor:
     * {@code GET https://wh.route4me.com/modules/api/v5.0/telematics/vendors?vendor_id=...}.
     *
     * <p>This replaces the deprecated {@link #getTelematicsVendorInfo(String)}, whose host
     * is retired.
     *
     * @param vendorId the vendor ID
     * @return the vendor
     * @throws APIException if the request fails
     */
    public TelematicsVendorV5 getTelematicsVendorV5(long vendorId) throws APIException {
        URIBuilder builder = v5Builder(TELEMATICS_VENDORS_V5);
        builder.setParameter("vendor_id", Long.toString(vendorId));
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendorV5.class);
    }

    /** Returns a builder for a V5 path on {@code wh.route4me.com}. */
    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    /** Sets a query parameter unless its value is {@code null}. */
    private static void setIfPresent(URIBuilder builder, String name, Object value) {
        if (value != null) {
            builder.setParameter(name, String.valueOf(value));
        }
    }

    /** Converts a flag to {@code 1}/{@code 0}, keeping {@code null}. */
    private static String flag(Boolean value) {
        return value == null ? null : (value ? "1" : "0");
    }

    /**
     * Compares the features of telematics vendors on {@code telematics.route4me.com}.
     *
     * @param vendorIDs the vendor IDs, separated by commas
     * @return never returns: the method always throws
     * @throws APIException always, without sending a request: the host is retired
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this method throws without sending a request (it would have sent
     *             the API key to that host). Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public List<TelematicsVendorComparison> compareTelematicsVendors(String vendorIDs) throws APIException {
        throw retiredHost();
    }

}
