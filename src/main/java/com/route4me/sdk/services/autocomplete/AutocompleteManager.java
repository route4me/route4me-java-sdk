package com.route4me.sdk.services.autocomplete;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Suggests addresses for a partly typed one through the V4 API ({@code https://api.route4me.com/autocomplete.php}).
 *
 * <p>No OpenAPI spec and no other SDK documents this endpoint; the {@code address} parameter follows the cURL example
 * and the Python SDK.
 */
public class AutocompleteManager extends Manager {

    private static final String AUTOCOMPLETE_EP = "/autocomplete.php";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public AutocompleteManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Gets address suggestions: {@code GET https://api.route4me.com/autocomplete.php?address=...}.
     *
     * @param address the partly typed address ({@code address}); without it the API answers
     *                {@code No address specified}
     * @return the answer, as text: JSON with {@code status} and {@code results}
     * @throws APIException if the request fails or the server answers with an error status
     */
    public String autocompleteAddress(String address) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(AUTOCOMPLETE_EP);
        if (address != null) {
            builder.setParameter("address", address);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, String.class);
    }
}
