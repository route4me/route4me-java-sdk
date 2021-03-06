/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Geocodings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.utils.URIBuilder;

/**
 *
 * @author juan
 */
public class GeocodingManager extends Manager {

    public static final String GEOCODING_EP = "/api/geocoder.php";
    public static final String GEOCODING_ADDRESS = "/api/address.php";
    private static final String JSON_GEOCODE = "/actions/upload/json-geocode.php";
    private int maxRetries = 0;

    public GeocodingManager(String apiKey) {
        super(apiKey);
    }

    public GeocoderResponse jsonUploadGeocoder(List<Address> addresses) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(JSON_GEOCODE);
        AddressesJSONBody body = new AddressesJSONBody(addresses);

        return makeJSONRequest(RequestMethod.POST, builder, body, null, GeocoderResponse.class);
    }

    public GeocoderWebSockets websocketsGeocoder(List<Address> addresses) {
        GeocoderWebSockets geocoder = new GeocoderWebSockets();
        try {
            GeocoderResponse job = jsonUploadGeocoder(addresses);
            String temporaryAddressesStorageID = job.getTemporaryAddressesStorageID();
            geocoder.startGeocoding(temporaryAddressesStorageID, job.getAddressCount());
        } catch (APIException ex) {
            Logger.getLogger(GeocodingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return geocoder;
    }

    public List<Address> bulkGeocoder(List<String> addresses, GeocoderOptions options) {
        ExecutorService executor = Executors.newFixedThreadPool(options.getMaxThreads());

        maxRetries = options.getMaxRetries();

        List<Future<Address>> workers = new ArrayList<>();
        List<Address> geocodedAddresses = new ArrayList<>();

        for (String address : addresses) {
            if (address != null && address.length() >= 3) {
                Callable callableWorker = new ThreadBasedGeocoding(this, address, options);
                Future<Address> future = executor.submit(callableWorker);
                workers.add(future);
            } else {
                Address invalidAddress = new Address(address, 0.0, 0.0);
                Geocodings defaultGeocodings = new Geocodings(0.0, 0.0, "invalid", "0", address);
                invalidAddress.setGeocodings(Arrays.asList(defaultGeocodings));
                geocodedAddresses.add(invalidAddress);
                logger.warn("Invalid Address: " + address);
            }
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        for (Future<Address> worker : workers) {
            Address geocodedAddress;
            try {
                geocodedAddress = worker.get(options.getMaxTimeout(), TimeUnit.MILLISECONDS);
                geocodedAddresses.add(geocodedAddress);
            } catch (InterruptedException | ExecutionException ex) {
                logger.error("Thread Execution Exception. " + ex);
            } catch (TimeoutException ex) {
                logger.error("Thread Time out Error. " + ex);
            }
        }

        return geocodedAddresses;

    }

    public Geocodings[] geocode(String address) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(GEOCODING_ADDRESS);
        builder.setParameter("address", address);
        builder.setParameter("format", "json");
        builder.setParameter("detailed", "true");
        return this.makeJSONRequest(RequestMethod.GET, builder, "", Geocodings[].class);
    }

    public <T> T forwardGeocode(URIBuilder builder, Class<T> clazz) {

        int retries = 0;
        while (retries < maxRetries) {
            try {
                return this.makeJSONRequest(RequestMethod.GET, builder, "", clazz);

            } catch (APIException ex) {
                try {
                    GeocoderAddressNotFound addressNotFound = this.gson.fromJson(ex.getMessage(), GeocoderAddressNotFound.class);
                    logger.debug(addressNotFound.getStatus());
                    break;
                } catch (IllegalStateException err) {
                    logger.error(ex);
                } catch (JsonSyntaxException err) {
                    logger.error(String.format("Unexpected Server response %s", ex.getMessage()));
                }
                retries++;
                logger.info("Retrying due to error: " + ex);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }

            }
        }
        return null;

    }

    @Getter
    @Setter
    private static class AddressesJSONBody {

        @SerializedName("rows")
        private List<Address> addresses;

        private AddressesJSONBody(List<Address> addresses) {
            this.addresses = addresses;
        }
    }
}
