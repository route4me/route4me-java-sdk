package com.route4me.sdk.services.addressbarcode;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Reads and saves the barcodes scanned at route destinations, through the V5 API
 * ({@code https://wh.route4me.com/modules/api/v5.0/address-barcodes}).
 */
public class AddressBarcodeManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ADDRESS_BARCODES_EP = V5_BASE + "/address-barcodes";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public AddressBarcodeManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
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

    /**
     * Lists the barcodes scanned at a route destination, one page at a time:
     * {@code GET https://wh.route4me.com/modules/api/v5.0/address-barcodes}.
     *
     * @param routeId the ID of the route; {@code null} leaves {@code route_id} out
     * @param routeDestinationId the ID of the route destination; {@code null} leaves {@code route_destination_id} out
     * @param limit the number of barcodes per page; {@code null} leaves {@code limit} out
     * @param cursor the {@code next_page_cursor} of the previous page, or {@code null} for the first page
     * @return the page of barcodes, with the cursor of the next one
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public GetAddressBarcodesResponse getAddressBarcodes(String routeId, Long routeDestinationId, Integer limit,
            String cursor) throws APIException {
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

    /**
     * Saves the barcodes scanned at a route destination: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/address-barcodes}, with the request as the JSON body.
     *
     * @param request the route, the destination and the barcodes
     * @return whether the barcodes were saved
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public StatusResponse saveAddressBarcodes(SaveAddressBarcodesRequest request) throws APIException {
        URIBuilder builder = v5Builder(ADDRESS_BARCODES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }
}
