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
     * @param routeId the ID of the route; required ({@code route_id})
     * @param routeDestinationId the ID of the route destination; {@code null} leaves {@code route_destination_id} out
     * @param limit the number of barcodes per page; {@code null} leaves {@code limit} out
     * @param cursor the {@code next_page_cursor} of the previous page, or {@code null} for the first page
     * @return the page of barcodes, with the cursor of the next one
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if {@code routeId} is {@code null} or empty; nothing is sent
     */
    public GetAddressBarcodesResponse getAddressBarcodes(String routeId, Long routeDestinationId, Integer limit,
            String cursor) throws APIException {
        requireText(routeId, "route_id");
        URIBuilder builder = v5Builder(ADDRESS_BARCODES_EP);
        builder.setParameter("route_id", routeId);
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
     * <p>The API stores the coordinates of a scan as {@code lat} and {@code lng}.
     *
     * @param request the route, the destination and the barcodes; {@code route_id}, {@code route_destination_id}
     *     and {@code barcodes} are required, and so are the {@code barcode} and {@code scan_type} of each barcode
     * @return whether the barcodes were saved
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if a required field is missing; nothing is sent
     */
    public StatusResponse saveAddressBarcodes(SaveAddressBarcodesRequest request) throws APIException {
        if (request == null) {
            throw new IllegalArgumentException("The request is required");
        }
        requireText(request.getRouteId(), "route_id");
        if (request.getRouteDestinationId() == null) {
            throw new IllegalArgumentException("route_destination_id is required");
        }
        if (request.getBarcodes() == null || request.getBarcodes().isEmpty()) {
            throw new IllegalArgumentException("barcodes is required");
        }
        for (BarcodeDataRequest barcode : request.getBarcodes()) {
            if (barcode == null) {
                throw new IllegalArgumentException("barcodes must not hold null");
            }
            requireText(barcode.getBarcode(), "barcodes[].barcode");
            requireText(barcode.getScanType(), "barcodes[].scan_type");
        }
        URIBuilder builder = v5Builder(ADDRESS_BARCODES_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(request), StatusResponse.class);
    }

    /** Rejects a required text that is {@code null} or empty, before any request is sent. */
    private static void requireText(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " is required");
        }
    }
}
