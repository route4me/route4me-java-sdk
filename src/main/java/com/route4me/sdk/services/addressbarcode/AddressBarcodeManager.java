package com.route4me.sdk.services.addressbarcode;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class AddressBarcodeManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ADDRESS_BARCODES_EP = V5_BASE + "/address-barcodes";

    public AddressBarcodeManager(String apiKey) {
        super(apiKey);
    }

    public AddressBarcodeManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    public GetAddressBarcodesResponse getAddressBarcodes(String routeId, Long routeDestinationId, Integer limit, String cursor) throws APIException {
        URIBuilder builder = v5Builder(ADDRESS_BARCODES_EP);
        if (routeId != null) {
            builder.setParameter("route_id", routeId);
        }
        if (routeDestinationId != null) {
            builder.setParameter("route_destination_id", String.valueOf(routeDestinationId));
        }
        if (limit != null) {
            builder.setParameter("limit", String.valueOf(limit));
        }
        if (cursor != null) {
            builder.setParameter("cursor", cursor);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, GetAddressBarcodesResponse.class);
    }

    public StatusResponse saveAddressBarcodes(SaveAddressBarcodesRequest request) throws APIException {
        URIBuilder builder = v5Builder(ADDRESS_BARCODES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }
}
