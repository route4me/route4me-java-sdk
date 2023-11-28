/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import static com.route4me.sdk.queryconverter.QueryConverter.convertObjectToParameters;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.utils.URIBuilder;

/**
 *
 * @author Route4Me
 */
public class TelematicsManager extends Manager {

    public static final String TELEMATICS_VENDORS_V4 = "/api/vendors.php";
    public static final String TELEMATICS_REGISTER_V4 = "/api.v4/telematics/register.php";
    public static final String TELEMATICS_CONNECTIONS_V4 = "/api.v4/telematics/connections.php";
    public static final String TELEMATICS_VENDORS_INFO_V4 = "/api.v4/telematics/vendors.php";

    public TelematicsManager(String apiKey) {
        super(apiKey);
    }

    private URIBuilder getTelematicsVendorURI() {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("telematics.route4me.com");
        builder.setPath(TELEMATICS_VENDORS_V4);
        return builder;
    }

    private URIBuilder getTelematicsVendorURI(String path) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("api.route4me.com");
        builder.setPath(path);
        return builder;
    }

    public TelematicsMemberRegister registerMember(Integer memberID) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_REGISTER_V4);
        builder.setParameter("member_id", memberID.toString());
        return makeRequest(RequestMethod.GET, builder, "", TelematicsMemberRegister.class);
    }

    public List<TelematicsConnection> getTelematicsConnections(String apiToken) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.GET, builder, "", List.class);

    }

    public TelematicsVendorsInfo getTelematicsVendorsInfo() throws APIException {
        URIBuilder builder = getTelematicsVendorURI(TELEMATICS_VENDORS_INFO_V4);
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendorsInfo.class);
    }

    public TelematicsVendorsInfo getTelematicsVendorsInfo(String apiToken) throws APIException {
        URIBuilder builder = getTelematicsVendorURI(TELEMATICS_VENDORS_INFO_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendorsInfo.class);
    }

    public TelematicsVendorInfo getTelematicsVendorInfo(String vendorID, String apiToken) throws APIException {
        URIBuilder builder = getTelematicsVendorURI(TELEMATICS_VENDORS_INFO_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("vendor_id", vendorID);
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendorInfo.class);
    }

    private TelematicsVendorsInfo searchVendor(String country, String size, String keyWord, String feature, String page, String perPage) throws APIException {
        URIBuilder builder = getTelematicsVendorURI();
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

    public TelematicsVendorsInfo searchVendorByCountry(String country) throws APIException {
        return searchVendorByCountry(country, null, null);
    }

    public TelematicsVendorsInfo searchVendorByCountry(String country, String page, String perPage) throws APIException {
        return searchVendor(country, null, null, null, page, perPage);
    }

    public TelematicsVendorsInfo searchVendorBySize(String size) throws APIException {
        return searchVendorByCountry(size, null, null);
    }

    public TelematicsVendorsInfo searchVendorBySize(String size, String page, String perPage) throws APIException {
        return searchVendor(null, size, null, null, page, perPage);
    }

    public TelematicsVendorsInfo searchVendorByKeyWord(String keyWord) throws APIException {
        return searchVendorByCountry(keyWord, null, null);
    }

    public TelematicsVendorsInfo searchVendorByKeyWord(String keyWord, String page, String perPage) throws APIException {
        return searchVendor(null, null, keyWord, null, page, perPage);
    }

    public TelematicsVendorsInfo searchVendorByFeature(String feature) throws APIException {
        return searchVendorByCountry(feature, null, null);
    }

    public TelematicsVendorsInfo searchVendorByFeature(String feature, String page, String perPage) throws APIException {
        return searchVendor(null, null, null, feature, page, perPage);
    }

    public TelematicsVendor[] getTelematicsVendor(String apiToken) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_VENDORS_INFO_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.GET, builder, "", TelematicsVendor[].class);
    }

    public TelematicsConnection createTelematicsConnection(String apiToken, TelematicsConnection credentials) throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        return makeRequest(RequestMethod.POST, builder, convertObjectToParameters(credentials), TelematicsConnection.class);

    }

    public TelematicsConnection updateTelematicsConnection(String apiToken, String connectionToken, TelematicsConnection credentials) throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.PUT, builder, convertObjectToParameters(credentials), TelematicsConnection.class);

    }

    public TelematicsConnection deleteTelematicsConnection(String apiToken, String connectionToken) throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.DELETE, builder, "", TelematicsConnection.class);

    }

    public TelematicsConnection getTelematicsConnection(String apiToken, String connectionToken) throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(TELEMATICS_CONNECTIONS_V4);
        builder.setParameter("api_token", apiToken);
        builder.setParameter("connection_token", connectionToken);
        return makeRequest(RequestMethod.GET, builder, "", TelematicsConnection.class);

    }

    private List<TelematicsVendorComparison> parseVendorFeaturesComparison(TelematicsVendorsInfo vendors) {
        List<TelematicsVendorComparison> comparison = new ArrayList<>();
        for (TelematicsVendorInfo vendor : vendors.getVendors()) {
            TelematicsVendorComparison c = new TelematicsVendorComparison();
            c.setId(vendor.getVendorID());
            c.setName(vendor.getName());
            List<String> features = new ArrayList<>();
            for (TelematicsVendorFeatures f : vendor.getFeatures()) {
                features.add(f.getName());
            }
            c.setFeatures(String.join(",", features));
            comparison.add(c);
        }
        return comparison;
    }

    public List<TelematicsVendorComparison> compareTelematicsVendors(String vendorIDs) throws APIException {
        URIBuilder builder = getTelematicsVendorURI();
        builder.setParameter("vendors", vendorIDs);
        TelematicsVendorsInfo vendors = makeRequest(RequestMethod.GET, builder, "", TelematicsVendorsInfo.class);
        return parseVendorFeaturesComparison(vendors);
    }

}
