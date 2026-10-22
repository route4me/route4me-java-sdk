/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.Gson;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.utils.Sanitizer;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Geocodes a stored batch of addresses over a Socket.IO connection to {@code https://validator.route4me.com} and
 * collects the results. {@link GeocodingManager#websocketsGeocoder(List)} creates and starts it. Each instance has
 * its own connection; close it with {@link #closeGeoCodeSocketIO()}.
 */
public class GeocoderWebSockets {

    private static final Logger LOG = Logger.getLogger(GeocoderWebSockets.class.getName());

    /**
     * The connection of this geocoder; {@code null} until {@link #startGeocoding} connects.
     * Each geocoder has its own, so that two jobs never share or close each other's socket.
     */
    private Socket geocoderWebSocket;

    private static final String GEOCODER_URL = "https://validator.route4me.com";

    private List<Address> geocodedAddresses;
    /** The JSON mapper of the socket messages. */
    @SuppressWarnings("checkstyle:VisibilityModifier") // protected API: kept for subclasses
    protected Gson gson = new Gson();

    private int addressesCount = 0;
    private int totalAddresses = 0;
    private long nextDownloadStage = 0;
    private String temporaryAddressesStorageID;

    /** The number of addresses to download in one stage, before the chunks are split. */
    private static final int BUFFER_FAIL_SAFE_MAX_ADDRESSES = 100;
    /** The smallest and largest chunk the validator is asked for. */
    private static final int MIN_CHUNK_SIZE = 10;
    private static final int MAX_CHUNK_SIZE = 200;

    // Set by startGeocoding, from the number of addresses of the job.
    private long chunkSize;
    private long chunksLimit;
    private long maxAddressesToBeDownloaded;

    /** Creates a geocoder that has not started; see {@link #startGeocoding(String, int)}. */
    public GeocoderWebSockets() {
        this.geocodedAddresses = null;
    }

    private Address addressParser(String data) {
        WebSocketsAddress webSocketAddress;
        webSocketAddress = gson.fromJson(data, WebSocketsAddress.class);
        return webSocketAddress.getAddress();
    }

    private void addressesParser(WebSocketsAddress[] addressesChunk) {
        for (WebSocketsAddress a : addressesChunk) {
            geocodedAddresses.add(a.getAddress());
        }
    }

    private void downloadAddresses(int start) {
        LOG.fine("Downloading Geocoded Addresses");

        this.nextDownloadStage = this.addressesCount + this.maxAddressesToBeDownloaded;
        if (geocoderWebSocket != null && geocoderWebSocket.connected()) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("temporary_addresses_storage_id", this.temporaryAddressesStorageID);
            parameters.put("from_index", start);
            parameters.put("chunks_limit", this.chunksLimit);
            parameters.put("chunk_size", this.chunkSize);
            try {
                JSONObject obj = new JSONObject(gson.toJson(parameters));
                geocoderWebSocket.emit("download", obj);
            } catch (JSONException ex) {
                Logger.getLogger(GeocoderWebSockets.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void connectGeocoder() {
        if (geocoderWebSocket == null || !geocoderWebSocket.connected()) {
            try {
                geocoderWebSocket = IO.socket(GEOCODER_URL);
                geocoderWebSocket.on(Socket.EVENT_CONNECT, new Listener() {
                    @Override
                    public void call(Object... os) {
                        LOG.info("Connected to the WebSocket Geocoder");
                        if (os.length > 0) {
                            LOG.fine(Sanitizer.forLog(os[0]));
                        }
                        emitTemporaryAddressesStorageID();
                    }

                }).on(Socket.EVENT_DISCONNECT, new Listener() {

                    @Override
                    public void call(Object... args) {
                        LOG.info("Now Disconnected from the WebSocket Geocoder");
                        if (args.length > 0) {
                            LOG.fine(Sanitizer.forLog(args[0]));
                        }
                    }

                }).on("connect_error", new Listener() {

                    @Override
                    public void call(Object... arg0) {
                        LOG.warning("Geocode socket connection error");
                        if (arg0.length > 0) {
                            LOG.warning(Sanitizer.forLog(arg0[0]));
                        }
                    }
                }).on("connect_timeout", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        LOG.warning("Geocode socket connection timeout error");
                        if (arg0.length > 0) {
                            LOG.warning(Sanitizer.forLog(arg0[0]));
                        }
                    }
                }).on("address", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        Address address = addressParser(arg0[0].toString());
                        if (address != null) {
                            geocodedAddresses.add(address);
                            addressesCount++;
                        }
                        if (getTotalAddresses() == getAddressesCount()) {
                            closeGeoCodeSocketIO();
                        }
                    }
                }).on("addresses_bulk", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        WebSocketsAddress[] addressesChunk = gson.fromJson(arg0[0].toString(),
                                WebSocketsAddress[].class);
                        addressesParser(addressesChunk);
                        addressesCount += addressesChunk.length;
                        LOG.log(Level.FINE, "Addresses Received: {0}", addressesChunk.length);
                        if (addressesCount == nextDownloadStage) {
                            downloadAddresses(addressesCount);
                        }
                        if (addressesCount == totalAddresses) {
                            closeGeoCodeSocketIO();
                        }
                    }
                }).on("geocode_progress", new Listener() {
                    @Override
                    public void call(Object... arg0) {
//                        LOG.info(arg0[0].toString());
                        GeocoderProgress progress = gson.fromJson(arg0[0].toString(), GeocoderProgress.class);
                        if (Objects.equals(progress.getDone(), progress.getTotal())) {
                            downloadAddresses(0);
                        }
                    }
                }).on("error", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        LOG.warning(String.valueOf(arg0.length));
                        if (arg0.length > 0) {
                            LOG.warning(Sanitizer.forLog(arg0[0]));
                        }
                    }
                });
                geocoderWebSocket.connect();
            } catch (URISyntaxException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        } else {
            LOG.info("Socket was already connected");
        }
    }

    /** Stops listening for addresses and closes the connection. Calling it before the geocoder started does nothing. */
    public void closeGeoCodeSocketIO() {
        if (geocoderWebSocket != null) {
            geocoderWebSocket.off("address");
            geocoderWebSocket.off("addresses_bulk");
            geocoderWebSocket.emit("disconnect");
            geocoderWebSocket.disconnect();
            geocoderWebSocket.close();
        }
    }

    private void emitTemporaryAddressesStorageID() {
        LOG.log(Level.INFO, "Emiting Temporary Addresses StorageID: {0}",
                Sanitizer.forLog(this.temporaryAddressesStorageID));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temporary_addresses_storage_id", this.temporaryAddressesStorageID);
        try {
            JSONObject obj = new JSONObject(gson.toJson(parameters));
            geocoderWebSocket.emit("geocode", obj);
        } catch (JSONException ex) {
            Logger.getLogger(GeocoderWebSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Connects to the websocket geocoder, if this geocoder is not connected yet, and asks it to geocode a stored batch;
     * the addresses then arrive in the background.
     *
     * @param storageId the ID of the stored addresses ({@code temporary_addresses_storage_id})
     * @param addressCount the number of stored addresses
     */
    public void startGeocoding(String storageId, int addressCount) {
        this.totalAddresses = addressCount;
        // One chunk per hundredth of the job, from 10 to 200 addresses, and as many chunks
        // as fill a stage of 100 addresses (the same rule as the C# SDK). Floating-point
        // division: with integer division the chunk was always 100.
        this.chunkSize = Math.round(Math.min(MAX_CHUNK_SIZE, Math.max(MIN_CHUNK_SIZE, addressCount / 100.0)));
        this.chunksLimit = (long) Math.ceil(BUFFER_FAIL_SAFE_MAX_ADDRESSES / (double) this.chunkSize);
        this.maxAddressesToBeDownloaded = this.chunkSize * this.chunksLimit;
        this.temporaryAddressesStorageID = storageId;
        geocodedAddresses = new ArrayList<>();
        if (geocoderWebSocket == null || !geocoderWebSocket.connected()) {
            connectGeocoder();
        }

    }

    /**
     * Tells whether every address of the batch has arrived.
     *
     * @return {@code true} when the number of addresses received equals the size of the batch
     */
    public Boolean isCompleted() {
        return getTotalAddresses() == getAddressesCount();
    }

    /**
     * Describes the progress, for display.
     *
     * @return a text such as {@code Addresses Processed: 120}
     */
    public String getProgress() {
        return "Addresses Processed: " + addressesCount;
    }

    /**
     * Returns the addresses received so far.
     *
     * @return the geocoded addresses; {@code null} before {@link #startGeocoding(String, int)}
     */
    public List<Address> getAddresses() {
        return geocodedAddresses;
    }

    /**
     * Returns how many addresses have arrived.
     *
     * @return the number of geocoded addresses received
     */
    public int getAddressesCount() {
        return addressesCount;
    }

    /**
     * Returns the size of the batch.
     *
     * @return the number of addresses to geocode
     */
    public int getTotalAddresses() {
        return totalAddresses;
    }

}
