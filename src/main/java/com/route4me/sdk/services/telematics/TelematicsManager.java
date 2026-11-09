/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import static com.route4me.sdk.queryconverter.QueryConverter.convertObjectToParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * Manages telematics integrations: the member registration and the vendor connections through the V4 API
 * ({@code https://api.route4me.com/api.v4/telematics}), and the vendor catalog and the creation and update of
 * connections through the V5 API ({@code https://wh.route4me.com/modules/api/v5.0/telematics}).
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
    /** The telematics connections on the V5 API: {@code POST}, and {@code PUT ...?connection_token=...}. */
    public static final String TELEMATICS_CONNECTIONS_V5 = "/modules/api/v5.0/telematics/connections";

    /**
     * The message of the {@link IllegalArgumentException} that the V4 connection methods throw for a connection
     * with a vendor API key.
     */
    static final String VENDOR_KEY_ON_V4_MESSAGE = "The V4 connection request carries the Route4Me API key as"
            + " api_key, so it does not send a vendor API key under the same name: use createTelematicsConnectionV5"
            + " or updateTelematicsConnectionV5, which send it as vendor_api_key, or set the vendor API key to null";

    /** Writes the connection metadata as JSON, keeping the keys whose value is {@code null}. */
    private static final Gson METADATA_JSON = new GsonBuilder().serializeNulls().create();

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
     * the connection as a form; {@code metadata} is sent as the text of a JSON object.
     *
     * <p>Like every V4 request, this one carries the Route4Me API key as the {@code api_key} query parameter, and the
     * telematics API reads a vendor API key under the same name. So that the two never meet in one request, a
     * connection with a vendor API key is refused: connect such a vendor with
     * {@link #createTelematicsConnectionV5(TelematicsConnection)}.
     *
     * @param apiToken the telematics API token of the member
     * @param credentials the vendor, the credentials and the options of the connection, without a vendor API key
     * @return the created connection
     * @throws IllegalArgumentException if {@code credentials} is {@code null} or has a vendor API key; nothing is sent
     * @throws IllegalAccessException if the connection cannot be read for the form
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsConnection createTelematicsConnection(String apiToken, TelematicsConnection credentials)
            throws IllegalAccessException, APIException {
        List<NameValuePair> form = connectionForm(credentials);
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.POST, builder, form, TelematicsConnection.class);

    }

    /**
     * Updates a telematics connection:
     * {@code PUT https://api.route4me.com/api.v4/telematics/connections.php?api_token=...&connection_token=...}, with
     * the connection as a form; {@code metadata} is sent as the text of a JSON object.
     *
     * <p>Like every V4 request, this one carries the Route4Me API key as the {@code api_key} query parameter, and the
     * telematics API reads a vendor API key under the same name. So that the two never meet in one request, a
     * connection with a vendor API key is refused: leave the vendor API key {@code null} to keep the stored one, or
     * change it with {@link #updateTelematicsConnectionV5(String, TelematicsConnection)}.
     *
     * @param apiToken the telematics API token of the member
     * @param connectionToken the token of the connection
     * @param credentials the new values, without a vendor API key
     * @return the updated connection
     * @throws IllegalArgumentException if {@code credentials} is {@code null} or has a vendor API key; nothing is sent
     * @throws IllegalAccessException if the connection cannot be read for the form
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsConnection updateTelematicsConnection(String apiToken, String connectionToken,
            TelematicsConnection credentials) throws IllegalAccessException, APIException {
        List<NameValuePair> form = connectionForm(credentials);
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.PUT, builder, form, TelematicsConnection.class);

    }

    /**
     * Builds the form of a V4 connection request: the {@link com.route4me.sdk.queryconverter.QueryParameter} fields
     * of the connection, and {@code metadata} as the text of a JSON object.
     *
     * @param credentials the connection
     * @return the form fields
     * @throws IllegalArgumentException if {@code credentials} is {@code null} or has a vendor API key
     * @throws IllegalAccessException if the connection cannot be read
     */
    private static List<NameValuePair> connectionForm(TelematicsConnection credentials)
            throws IllegalAccessException {
        requireConnection(credentials);
        if (credentials.getApiKey() != null) {
            throw new IllegalArgumentException(VENDOR_KEY_ON_V4_MESSAGE);
        }
        List<NameValuePair> form = convertObjectToParameters(credentials);
        if (credentials.getMetadata() != null) {
            form.add(new BasicNameValuePair("metadata", metadataJson(credentials.getMetadata())));
        }
        return form;
    }

    /**
     * Connects the account to a telematics vendor:
     * {@code POST https://wh.route4me.com/modules/api/v5.0/telematics/connections}, with the connection as the JSON
     * body. The Route4Me API key goes in the {@code Authorization: Bearer} header, and the vendor API key, if any, as
     * {@code vendor_api_key}, which the API forwards to the telematics service as the vendor's {@code api_key}; no
     * telematics API token is needed.
     *
     * <p>The body carries the fields that are set: {@code vendor}, {@code vendor_id}, {@code name}, {@code host},
     * {@code vendor_api_key}, {@code account_id}, {@code username}, {@code password},
     * {@code vehicle_position_refresh_rate}, {@code max_idle_time}, {@code is_enabled} and
     * {@code validate_remote_credentials} (as JSON booleans when they are {@code 1}/{@code 0} or
     * {@code "true"}/{@code "false"}), and {@code metadata} as the text of a JSON object. The vendor credential fields
     * each vendor needs are listed by {@link #getTelematicsVendorV5(long)}.
     *
     * @param connection the vendor, the credentials and the options of the connection
     * @return the created connection
     * @throws IllegalArgumentException if {@code connection} is {@code null}; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsConnection createTelematicsConnectionV5(TelematicsConnection connection) throws APIException {
        requireConnection(connection);
        URIBuilder builder = v5Builder(TELEMATICS_CONNECTIONS_V5);
        return makeRequest(RequestMethod.POST, builder, this.gson.toJson(connectionBodyV5(connection)),
                TelematicsConnection.class);
    }

    /**
     * Updates a telematics connection:
     * {@code PUT https://wh.route4me.com/modules/api/v5.0/telematics/connections?connection_token=...}, with the new
     * values as the JSON body, built as in {@link #createTelematicsConnectionV5(TelematicsConnection)}. Fields that are
     * not set keep their stored values, except {@code is_enabled}, which the API sets to true when it is not sent.
     *
     * @param connectionToken the token of the connection
     * @param connection the new values
     * @return the updated connection
     * @throws IllegalArgumentException if {@code connectionToken} is {@code null} or empty, or {@code connection} is
     *     {@code null}; nothing is sent
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public TelematicsConnection updateTelematicsConnectionV5(String connectionToken, TelematicsConnection connection)
            throws APIException {
        if (connectionToken == null || connectionToken.isEmpty()) {
            throw new IllegalArgumentException("The connection token is required");
        }
        requireConnection(connection);
        URIBuilder builder = v5Builder(TELEMATICS_CONNECTIONS_V5);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.PUT, builder, this.gson.toJson(connectionBodyV5(connection)),
                TelematicsConnection.class);
    }

    /** Rejects a {@code null} connection, before any request is sent. */
    private static void requireConnection(TelematicsConnection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("The connection is required");
        }
    }

    /** Builds the JSON body of a V5 connection request; see {@link #createTelematicsConnectionV5}. */
    private static JsonObject connectionBodyV5(TelematicsConnection connection) {
        JsonObject body = new JsonObject();
        addIfPresent(body, "vendor", connection.getVendor());
        addIfPresent(body, "vendor_id", connection.getVendorID());
        addIfPresent(body, "name", connection.getName());
        addIfPresent(body, "host", connection.getHost());
        addIfPresent(body, "vendor_api_key", connection.getApiKey());
        addIfPresent(body, "account_id", connection.getAccountID());
        addIfPresent(body, "username", connection.getUsername());
        addIfPresent(body, "password", connection.getPassword());
        addIfPresent(body, "vehicle_position_refresh_rate", connection.getVehiclePositionRefreshRate());
        addIfPresent(body, "max_idle_time", connection.getMaxIdleTime());
        if (connection.getIsEnabled() != null) {
            body.addProperty("is_enabled", connection.getIsEnabled() != 0);
        }
        addFlag(body, "validate_remote_credentials", connection.getValidateRemoteCredentials());
        if (connection.getMetadata() != null) {
            body.addProperty("metadata", metadataJson(connection.getMetadata()));
        }
        return body;
    }

    /** Adds a text member unless its value is {@code null}. */
    private static void addIfPresent(JsonObject body, String name, String value) {
        if (value != null) {
            body.addProperty(name, value);
        }
    }

    /** Adds a number member unless its value is {@code null}. */
    private static void addIfPresent(JsonObject body, String name, Number value) {
        if (value != null) {
            body.addProperty(name, value);
        }
    }

    /**
     * Adds a flag kept as text: {@code "1"}/{@code "true"} and {@code "0"}/{@code "false"} become JSON booleans,
     * any other text is sent as it is, and {@code null} is left out.
     */
    private static void addFlag(JsonObject body, String name, String value) {
        if (value == null) {
            return;
        }
        String text = value.trim().toLowerCase(Locale.ROOT);
        if ("1".equals(text) || "true".equals(text)) {
            body.addProperty(name, true);
        } else if ("0".equals(text) || "false".equals(text)) {
            body.addProperty(name, false);
        } else {
            body.addProperty(name, value);
        }
    }

    /** Returns the connection metadata as the text of a JSON object, {@code null} values included. */
    private static String metadataJson(Map<String, Object> metadata) {
        return METADATA_JSON.toJson(metadata);
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
