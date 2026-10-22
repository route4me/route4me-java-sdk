/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Geocodings;
import com.route4me.sdk.utils.Sanitizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * Geocodes addresses through the V4 API ({@code https://api.route4me.com}): one address at a time with {@link
 * #geocode(String)}, several in one request with {@link #batchGeocoding(String, String)} or {@link
 * #geocodeAddresses(String, String)}, many in parallel with {@link #bulkGeocoder(List, GeocoderOptions)}, or as an
 * uploaded batch that {@link GeocoderWebSockets} geocodes over a websocket.
 */
public class GeocodingManager extends Manager {

    /** The path of the batch geocoder, on {@code api.route4me.com}. */
    public static final String GEOCODING_EP = "/api/geocoder.php";
    /** The path that geocodes one address, on {@code api.route4me.com}. */
    public static final String GEOCODING_ADDRESS = "/api/address.php";
    private static final String JSON_GEOCODE = "/actions/upload/json-geocode.php";
    /** Time between two attempts of {@link #forwardGeocode(URIBuilder, Class, int)}, in milliseconds. */
    private static final long RETRY_DELAY_MS = 200;
    /** The retry count of the last {@link #bulkGeocoder} call, used by {@link #forwardGeocode(URIBuilder, Class)}. */
    private volatile int maxRetries = 0;

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public GeocodingManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Uploads addresses for the websocket geocoder: {@code POST
     * https://api.route4me.com/actions/upload/json-geocode.php}, with
     * {@code {"rows": [...]}} as the JSON body.
     *
     * @param addresses the addresses to geocode
     * @return the ID of the stored addresses and their number
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public GeocoderResponse jsonUploadGeocoder(List<Address> addresses) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(JSON_GEOCODE);
        AddressesJSONBody body = new AddressesJSONBody(addresses);

        return makeJSONRequest(RequestMethod.POST, builder, body, null, GeocoderResponse.class);
    }

    /**
     * Geocodes several addresses in one request: {@code POST https://api.route4me.com/api/geocoder.php?format=...},
     * with {@code addresses} and {@code strExportFormat} as form fields. Send coordinates ({@code lat,lng}) instead of
     * addresses to reverse geocode them.
     *
     * <p>C# {@code Route4MeManager.Geocoding} sends {@code addresses} and {@code format} in the query, and C#
     * {@code BatchGeocoding} and the Node.js SDK send {@code addresses} and {@code strExportFormat} as a form; this
     * method sends the format in both places and the addresses as a form, so long lists fit.
     *
     * @param addresses the addresses, separated by new lines or {@code ||}
     * @param format the format of the answer: {@code json}, {@code xml} or {@code csv}; {@code null} leaves it to
     *               the API
     * @return the answer as text, in the requested format
     * @throws APIException if the request fails or the server answers with an error status
     */
    public String batchGeocoding(String addresses, String format) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(GEOCODING_EP);
        List<NameValuePair> form = new ArrayList<>();
        if (format != null) {
            builder.setParameter("format", format);
            form.add(new BasicNameValuePair("strExportFormat", format));
        }
        if (addresses != null) {
            form.add(new BasicNameValuePair("addresses", addresses));
        }
        return this.makeRequest(RequestMethod.POST, builder, form, String.class);
    }

    /**
     * Geocodes addresses, or reverse geocodes coordinates, with the query only: {@code GET
     * https://api.route4me.com/api/geocoder.php?addresses=...&format=...}, as the Node.js SDK's {@code
     * Geocoding.forward} and {@code Geocoding.reverse} send it.
     *
     * @param addresses the addresses, or coordinates as {@code lat,lng} ({@code addresses})
     * @param format the format of the answer: {@code json}, {@code xml} or {@code csv} ({@code format}); {@code null}
     *               leaves it to the API
     * @return the answer as text, in the requested format
     * @throws APIException if the request fails or the server answers with an error status
     */
    public String geocodeAddresses(String addresses, String format) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(GEOCODING_EP);
        if (addresses != null) {
            builder.setParameter("addresses", addresses);
        }
        if (format != null) {
            builder.setParameter("format", format);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, String.class);
    }

    /**
     * Uploads addresses with {@link #jsonUploadGeocoder(List)} and starts geocoding them over a websocket to
     * {@code https://validator.route4me.com}. The call returns at once; poll {@link GeocoderWebSockets#isCompleted()}
     * and read {@link GeocoderWebSockets#getAddresses()}. If the upload fails, or its reply has no storage ID or
     * address count, the error is logged and the geocoder returned has not started.
     *
     * @param addresses the addresses to geocode
     * @return the geocoder of the job
     */
    public GeocoderWebSockets websocketsGeocoder(List<Address> addresses) {
        GeocoderWebSockets geocoder = new GeocoderWebSockets();
        try {
            GeocoderResponse job = jsonUploadGeocoder(addresses);
            if (job == null || job.getTemporaryAddressesStorageID() == null || job.getAddressCount() == null) {
                Logger.getLogger(GeocodingManager.class.getName()).log(Level.SEVERE,
                        "The address upload returned no storage ID or address count; the geocoder was not started");
                return geocoder;
            }
            String temporaryAddressesStorageID = job.getTemporaryAddressesStorageID();
            geocoder.startGeocoding(temporaryAddressesStorageID, job.getAddressCount());
        } catch (APIException ex) {
            Logger.getLogger(GeocodingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return geocoder;
    }

    /**
     * Geocodes many addresses in parallel with {@code GET /api/address.php}, one request
     * per address.
     *
     * <p>The result has one address per input, in the same order. An input that is
     * {@code null} or shorter than 3 characters, that the geocoder does not match, or whose
     * geocoding fails, times out or is interrupted comes back unmatched: latitude and
     * longitude 0 and a single geocoding of type {@code "invalid"}.
     *
     * <p>Up to {@link GeocoderOptions#getMaxThreads()} requests run at once; the connection
     * pool of this manager grows to that many connections if it is smaller. The call waits
     * at most {@link GeocoderOptions#getMaxTimeout()} milliseconds for each round of that
     * many addresses, then cancels what is still running. Each address gets up to
     * {@link GeocoderOptions#getMaxRetries()} attempts (at least one). If the calling
     * thread is interrupted, the call stops waiting, cancels the remaining work, returns
     * what it has and keeps the thread's interrupt flag set.
     *
     * <p>The retry count also becomes the default of {@link #forwardGeocode(URIBuilder, Class)}.
     *
     * @param addresses the addresses to geocode
     * @param options   threads, retries, timeout and response details
     * @return one address per input, in input order
     */
    public List<Address> bulkGeocoder(List<String> addresses, GeocoderOptions options) {
        maxRetries = options.getMaxRetries();

        Address[] results = new Address[addresses.size()];
        List<Callable<Address>> tasks = new ArrayList<>();
        List<Integer> taskIndexes = new ArrayList<>();
        for (int i = 0; i < addresses.size(); i++) {
            String address = addresses.get(i);
            if (address != null && address.length() >= 3) {
                tasks.add(new ThreadBasedGeocoding(this, address, options));
                taskIndexes.add(i);
            } else {
                results[i] = unmatched(address);
                logger.warn("Invalid Address: {}", Sanitizer.forLog(address));
            }
        }
        if (!tasks.isEmpty()) {
            int threads = Math.max(1, Math.min(options.getMaxThreads(), tasks.size()));
            // One connection per thread, or the extra threads wait for a free one.
            this.ensureMaxConnections(threads);
            ExecutorService executor = Executors.newFixedThreadPool(threads);
            try {
                List<Future<Address>> futures;
                if (options.getMaxTimeout() > 0) {
                    long rounds = (tasks.size() + threads - 1) / threads;
                    futures = executor.invokeAll(tasks, options.getMaxTimeout() * rounds, TimeUnit.MILLISECONDS);
                } else {
                    futures = executor.invokeAll(tasks);
                }
                for (int t = 0; t < futures.size(); t++) {
                    int index = taskIndexes.get(t);
                    results[index] = resultOf(futures.get(t), addresses.get(index));
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                logger.warn("Bulk geocoding was interrupted; the remaining addresses are returned unmatched");
            } finally {
                executor.shutdownNow();
            }
        }

        List<Address> geocodedAddresses = new ArrayList<>(results.length);
        for (int i = 0; i < results.length; i++) {
            geocodedAddresses.add(results[i] != null ? results[i] : unmatched(addresses.get(i)));
        }
        return geocodedAddresses;
    }

    /** Returns the address a finished task produced, or the unmatched default when it failed. */
    private static Address resultOf(Future<Address> future, String address) {
        try {
            Address geocoded = future.get();
            if (geocoded != null) {
                return geocoded;
            }
        } catch (CancellationException ex) {
            logger.error("Thread Time out Error. Geocoding did not finish in time: {}", Sanitizer.forLog(address));
        } catch (ExecutionException ex) {
            logger.error("Thread Execution Exception. {}", Sanitizer.forLog(ex.getCause()));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return unmatched(address);
    }

    /**
     * Builds the result for an address that could not be geocoded: latitude and longitude
     * 0 and one geocoding of type {@code "invalid"}.
     *
     * @param address the input address
     * @return the unmatched address
     */
    static Address unmatched(String address) {
        Address unmatched = new Address(address, 0.0, 0.0);
        unmatched.setGeocodings(Arrays.asList(new Geocodings(0.0, 0.0, "invalid", "0", address)));
        return unmatched;
    }

    /**
     * Geocodes one address: {@code GET https://api.route4me.com/api/address.php?address=...&format=json&detailed=true}.
     *
     * @param address the address
     * @return the matches, best first
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Geocodings[] geocode(String address) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(GEOCODING_ADDRESS);
        builder.setParameter("address", address);
        builder.setParameter("format", "json");
        builder.setParameter("detailed", "true");
        return this.makeJSONRequest(RequestMethod.GET, builder, "", Geocodings[].class);
    }

    /**
     * Sends one geocoding request, {@code GET} of the given URL (the bulk geocoder uses
     * {@code https://api.route4me.com/api/address.php?address=...}), retrying on errors, with the retry count of the
     * last {@link #bulkGeocoder} call (0 until then, which means one attempt).
     *
     * @param builder the request URL; it is not changed, so it can be sent again
     * @param clazz   the response type, such as {@code Geocodings[].class}
     * @param <T>     the response type
     * @return the parsed response, or {@code null} when the address is not found or every
     * attempt fails
     * @see #forwardGeocode(URIBuilder, Class, int)
     */
    public <T> T forwardGeocode(URIBuilder builder, Class<T> clazz) {
        return this.forwardGeocode(builder, clazz, this.maxRetries);
    }

    /**
     * Sends one geocoding request, {@code GET} of the given URL (the bulk geocoder uses
     * {@code https://api.route4me.com/api/address.php?address=...}), retrying on errors.
     *
     * <p>A response that says the address was not found ({@code {"status": ..., "results": ...}}) ends the attempts at
     * once. Attempts are 200 ms apart. If the thread is interrupted while it waits, it gives up and keeps its
     * interrupt flag set.
     *
     * @param builder    the request URL; it is not changed, so it can be sent again
     * @param clazz      the response type, such as {@code Geocodings[].class}
     * @param maxAttempts the maximum number of attempts; 0 and 1 both mean one attempt
     * @param <T>        the response type
     * @return the parsed response, or {@code null} when the address is not found or every
     * attempt fails
     */
    public <T> T forwardGeocode(URIBuilder builder, Class<T> clazz, int maxAttempts) {
        int attempts = Math.max(1, maxAttempts);
        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                return this.makeJSONRequest(RequestMethod.GET, builder, "", clazz);
            } catch (APIException ex) {
                if (isAddressNotFound(ex)) {
                    logger.debug("Address not found: {}", Sanitizer.forLog(ex.getResponseBody()));
                    return null;
                }
                if (attempt == attempts) {
                    logger.error("Geocoding failed after {} attempt(s): {}", attempts,
                            Sanitizer.forLog(ex.getMessage()));
                    return null;
                }
                logger.info("Retrying due to error: {}", Sanitizer.forLog(ex.getMessage()));
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        }
        return null;
    }

    /** Tells whether a failed request got the geocoder's "address not found" answer. */
    private boolean isAddressNotFound(APIException ex) {
        String body = ex.getResponseBody();
        if (body == null || body.isEmpty()) {
            return false;
        }
        try {
            GeocoderAddressNotFound notFound = this.gson.fromJson(body, GeocoderAddressNotFound.class);
            return notFound != null && notFound.getStatus() != null && notFound.getResults() != null;
        } catch (JsonParseException | IllegalStateException | ClassCastException err) {
            return false;
        }
    }

    @Getter
    @Setter
    private static final class AddressesJSONBody {

        /** The addresses to geocode ({@code rows}). */
        @SerializedName("rows")
        private List<Address> addresses;

        private AddressesJSONBody(List<Address> addresses) {
            this.addresses = addresses;
        }
    }
}
