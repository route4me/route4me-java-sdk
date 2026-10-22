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
 *
 * @author Route4Me
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
    public static final String TELEMATICS_REGISTER_V4 = "/api.v4/telematics/register.php";
    public static final String TELEMATICS_CONNECTIONS_V4 = "/api.v4/telematics/connections.php";
    public static final String TELEMATICS_VENDORS_INFO_V4 = "/api.v4/telematics/vendors.php";
    /** One vendor on the V5 API: {@code GET ...?vendor_id=...}. */
    public static final String TELEMATICS_VENDORS_V5 = "/modules/api/v5.0/telematics/vendors";
    /** The vendor catalog on the V5 API. */
    public static final String TELEMATICS_VENDORS_ALL_V5 = "/modules/api/v5.0/telematics/vendors/all";

    public TelematicsManager(String apiKey) {
        super(apiKey);
    }

    /** Returns a builder for the retired vendors endpoint of {@code telematics.route4me.com}. */
    private URIBuilder getTelemaricsVendorURI() {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("telematics.route4me.com");
        builder.setPath(TELEMATICS_VENDORS_V4);
        return builder;
    }

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
     * @return the vendors
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo getTelematicsVendorsInfo() throws APIException {
        URIBuilder builder = getTelemaricsVendorURI();
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendorsInfo.class);

    }

    /**
     * Gets one telematics vendor on {@code telematics.route4me.com}.
     *
     * @param vendorID the vendor ID
     * @return the vendor
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use {@link #getTelematicsVendorV5(long)}.
     */
    @Deprecated
    public TelematicsVendorsInfo getTelematicsVendorInfo(String vendorID) throws APIException {
        URIBuilder builder = getTelemaricsVendorURI();
        builder.setParameter("vendor_id", vendorID);
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendorsInfo.class);

    }

    private TelematicsVendorsInfo searchVendor(String country, String size, String keyWord, String feature,
            String page, String perPage) throws APIException {
        URIBuilder builder = getTelemaricsVendorURI();
        if (country != null) {
            builder.setParameter("country", country);
        }
        if (size != null) {
            builder.setParameter("size", size);
        }
        if (keyWord != null) {
            builder.setParameter("s", keyWord);
        }
        if (perPage != null) {
            builder.setParameter("per_page", perPage);
        }
        if (page != null) {
            builder.setParameter("page", page);
        }
        if (feature != null) {
            builder.setParameter("feature", feature);
        }
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendorsInfo.class);

    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a country code.
     *
     * @param country a country code
     * @return the matching vendors
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByCountry(String country) throws APIException {
        return searchVendorByCountry(country, null, null);
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a country code, one page
     * at a time.
     *
     * @param country a country code
     * @param page    the page number, or {@code null}
     * @param perPage the page size, or {@code null}
     * @return the matching vendors
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByCountry(String country, String page, String perPage)
            throws APIException {
        return searchVendor(country, null, null, null, page, perPage);
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a vendor size.
     *
     * @param size a vendor size
     * @return the matching vendors
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorBySize(String size) throws APIException {
        return searchVendorBySize(size, null, null);
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a vendor size, one page
     * at a time.
     *
     * @param size a vendor size
     * @param page    the page number, or {@code null}
     * @param perPage the page size, or {@code null}
     * @return the matching vendors
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorBySize(String size, String page, String perPage) throws APIException {
        return searchVendor(null, size, null, null, page, perPage);
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a keyword.
     *
     * @param keyWord a keyword
     * @return the matching vendors
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByKeyWord(String keyWord) throws APIException {
        return searchVendorByKeyWord(keyWord, null, null);
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a keyword, one page
     * at a time.
     *
     * @param keyWord a keyword
     * @param page    the page number, or {@code null}
     * @param perPage the page size, or {@code null}
     * @return the matching vendors
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByKeyWord(String keyWord, String page, String perPage)
            throws APIException {
        return searchVendor(null, null, keyWord, null, page, perPage);
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a feature.
     *
     * @param feature a feature
     * @return the matching vendors
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByFeature(String feature) throws APIException {
        return searchVendorByFeature(feature, null, null);
    }

    /**
     * Searches the telematics vendors on {@code telematics.route4me.com} by a feature, one page
     * at a time.
     *
     * @param feature a feature
     * @param page    the page number, or {@code null}
     * @param perPage the page size, or {@code null}
     * @return the matching vendors
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public TelematicsVendorsInfo searchVendorByFeature(String feature, String page, String perPage)
            throws APIException {
        return searchVendor(null, null, null, feature, page, perPage);
    }

    public TelematicsVendor[] getTelematicsVendor(String apiToken) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_VENDORS_INFO_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendor[].class);
    }

    public TelematicsConnection createTelematicsConnection(String apiToken, TelematicsConnection credentials)
            throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.POST, builder, convertObjectToParameters(credentials),
                TelematicsConnection.class);

    }

    public TelematicsConnection updateTelematicsConnection(String apiToken, String connectionToken,
            TelematicsConnection credentials) throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.PUT, builder, convertObjectToParameters(credentials),
                TelematicsConnection.class);

    }

    public TelematicsConnection deleteTelematicsConnection(String apiToken, String connectionToken)
            throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.DELETE, builder, "", TelematicsConnection.class);

    }

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

    private List<TelematicsVendorComparison> parseVendorFeaturesComparison(TelematicsVendorsInfo vendors) {
        List<TelematicsVendorComparison> comparison = new ArrayList<>();
        if (vendors == null || vendors.getVendors() == null) {
            return comparison;
        }
        for (TelematicsVendorInfo vendor : vendors.getVendors()) {
            if (vendor == null) {
                continue;
            }
            TelematicsVendorComparison c = new TelematicsVendorComparison();
            c.setId(vendor.getVendorID());
            c.setName(vendor.getName());
            List<String> features = new ArrayList<>();
            if (vendor.getFeatures() != null) {
                for (TelematicsVendorFeatures f : vendor.getFeatures()) {
                    if (f != null) {
                        features.add(f.getName());
                    }
                }
            }
            c.setFeatures(String.join(",", features));
            comparison.add(c);
        }
        return comparison;
    }

    /**
     * Compares the features of telematics vendors on {@code telematics.route4me.com}.
     *
     * @param vendorIDs the vendor IDs, separated by commas
     * @return one comparison per vendor
     * @throws APIException if the request fails
     * @deprecated {@code telematics.route4me.com} is retired and redirects to the Route4Me
     *             website, so this call cannot succeed. Use
     *             {@link #getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
     */
    @Deprecated
    public List<TelematicsVendorComparison> compareTelematicsVendors(String vendorIDs) throws APIException {
        URIBuilder builder = getTelemaricsVendorURI();
        builder.setParameter("vendors", vendorIDs);
        TelematicsVendorsInfo vendors = makeRequest(RequestMethod.GET, builder, "", TelematicsVendorsInfo.class);
        return parseVendorFeaturesComparison(vendors);
    }

}
