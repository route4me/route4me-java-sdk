/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.utils.LenientBooleanAdapter;
import com.route4me.sdk.utils.LenientMapAdapterFactory;
import com.route4me.sdk.utils.Sanitizer;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Geocodes a stored batch of addresses over a Socket.IO connection to {@code https://validator.route4me.com} and
 * collects the results. {@link GeocodingManager#websocketsGeocoder(List)} creates and starts it. Each instance has
 * its own connection; close it with {@link #closeGeoCodeSocketIO()}.
 *
 * <p>One instance runs one job: {@link #startGeocoding(String, int)} can be called once, and a second call throws
 * {@link IllegalStateException}. Create a new geocoder for another batch.
 *
 * <p>The addresses arrive on the Socket.IO event thread. Every public method can be called from any thread: the
 * state is guarded by the lock of this object, and {@link #getAddresses()} returns a copy.
 */
public class GeocoderWebSockets {

    private static final Logger LOG = LogManager.getLogger(GeocoderWebSockets.class);

    /**
     * The connection of this geocoder; {@code null} until {@link #startGeocoding} connects.
     * Each geocoder has its own, so that two jobs never share or close each other's socket.
     */
    private Socket geocoderWebSocket;

    private static final String GEOCODER_URL = "https://validator.route4me.com";

    /** The addresses received so far; guarded by the lock of this object. */
    private final List<Address> geocodedAddresses = new ArrayList<>();
    /** The JSON mapper of the socket messages. */
    @SuppressWarnings("checkstyle:VisibilityModifier") // protected API: kept for subclasses
    protected Gson gson = LenientMapAdapterFactory.register(LenientBooleanAdapter.register(new GsonBuilder())).create();

    private int addressesCount = 0;
    private int totalAddresses = 0;
    private long nextDownloadStage = 0;
    private String temporaryAddressesStorageID;
    private boolean started;
    /** Whether the {@code geocode} request of the job was sent; guarded by the lock of this object. */
    private boolean geocodeSent;

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
        // The job starts with startGeocoding.
    }

    /**
     * Returns the text of the first argument of a socket message, or {@code null} when there is none.
     *
     * @param args  the arguments of the message
     * @param event the name of the message, for the log
     * @return the payload as text, or {@code null}
     */
    private static String payloadOf(Object[] args, String event) {
        if (args == null || args.length == 0 || args[0] == null) {
            LOG.warn("The geocoder sent a {} message without a payload; it is ignored", event);
            return null;
        }
        return args[0].toString();
    }

    /**
     * Reads a socket message, or returns {@code null} and logs a warning when it cannot be read.
     *
     * @param payload the payload, as text
     * @param type    the type to read
     * @param event   the name of the message, for the log
     * @param <T>     the type to read
     * @return the message, or {@code null} when the payload is {@code null} JSON or cannot be read
     */
    private <T> T parse(String payload, Class<T> type, String event) {
        try {
            return gson.fromJson(payload, type);
        } catch (JsonParseException | IllegalStateException ex) {
            LOG.warn("The geocoder sent a {} message that cannot be read; it is ignored: {}", event,
                    Sanitizer.forLog(ex.getMessage()));
            return null;
        }
    }

    /**
     * Handles an {@code address} message: one geocoded address. Every message with a payload counts as one address
     * received, as each entry of {@link #onAddressesBulk} does, so that the job completes; only a readable address
     * is added to the list. A message without a payload is ignored.
     *
     * @param args the arguments of the message
     */
    synchronized void onAddress(Object... args) {
        String payload = payloadOf(args, "address");
        if (payload == null) {
            return;
        }
        WebSocketsAddress message = parse(payload, WebSocketsAddress.class, "address");
        Address address = message == null ? null : message.getAddress();
        if (address != null) {
            geocodedAddresses.add(address);
        }
        addressesCount++;
        if (totalAddresses == addressesCount) {
            closeGeoCodeSocketIO();
        }
    }

    /**
     * Handles an {@code addresses_bulk} message: a chunk of geocoded addresses. Every entry of the chunk counts as
     * received, so that the next download starts at the right index; entries without an address are not added to
     * the list. A message that is not a list is ignored.
     *
     * @param args the arguments of the message
     */
    synchronized void onAddressesBulk(Object... args) {
        String payload = payloadOf(args, "addresses_bulk");
        if (payload == null) {
            return;
        }
        WebSocketsAddress[] addressesChunk = parse(payload, WebSocketsAddress[].class, "addresses_bulk");
        if (addressesChunk == null) {
            return;
        }
        for (WebSocketsAddress entry : addressesChunk) {
            if (entry != null && entry.getAddress() != null) {
                geocodedAddresses.add(entry.getAddress());
            }
        }
        addressesCount += addressesChunk.length;
        LOG.debug("Addresses received: {}", addressesChunk.length);
        if (addressesCount == nextDownloadStage) {
            downloadAddresses(addressesCount);
        }
        if (addressesCount == totalAddresses) {
            closeGeoCodeSocketIO();
        }
    }

    /**
     * Handles a {@code geocode_progress} message: when every address is geocoded, starts the download. A message
     * without {@code done} and {@code total} is ignored.
     *
     * @param args the arguments of the message
     */
    synchronized void onGeocodeProgress(Object... args) {
        String payload = payloadOf(args, "geocode_progress");
        if (payload == null) {
            return;
        }
        GeocoderProgress progress = parse(payload, GeocoderProgress.class, "geocode_progress");
        if (progress != null && progress.getDone() != null && progress.getDone().equals(progress.getTotal())) {
            downloadAddresses(0);
        }
    }

    private synchronized void downloadAddresses(int start) {
        LOG.debug("Downloading the geocoded addresses");

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
                LOG.error("Could not build the download message: {}", Sanitizer.forLog(ex.getMessage()));
            }

        }
    }

    /**
     * Creates the socket of this geocoder, not connected yet.
     *
     * @return the socket
     * @throws URISyntaxException if the geocoder URL is not valid
     */
    Socket newSocket() throws URISyntaxException {
        return IO.socket(GEOCODER_URL);
    }

    private void connectGeocoder() {
        try {
            geocoderWebSocket = newSocket();
            geocoderWebSocket.on(Socket.EVENT_CONNECT, new Listener() {
                @Override
                public void call(Object... os) {
                    LOG.info("Connected to the WebSocket Geocoder");
                    if (os.length > 0) {
                        LOG.debug(Sanitizer.forLog(os[0]));
                    }
                    emitTemporaryAddressesStorageID();
                }

            }).on(Socket.EVENT_DISCONNECT, new Listener() {

                @Override
                public void call(Object... args) {
                    LOG.info("Now Disconnected from the WebSocket Geocoder");
                    if (args.length > 0) {
                        LOG.debug(Sanitizer.forLog(args[0]));
                    }
                }

            }).on("connect_error", new Listener() {

                @Override
                public void call(Object... arg0) {
                    LOG.warn("Geocode socket connection error: {}", arg0.length > 0 ? Sanitizer.forLog(arg0[0]) : "");
                }
            }).on("connect_timeout", new Listener() {
                @Override
                public void call(Object... arg0) {
                    LOG.warn("Geocode socket connection timeout error: {}",
                            arg0.length > 0 ? Sanitizer.forLog(arg0[0]) : "");
                }
            }).on("address", new Listener() {
                @Override
                public void call(Object... arg0) {
                    onAddress(arg0);
                }
            }).on("addresses_bulk", new Listener() {
                @Override
                public void call(Object... arg0) {
                    onAddressesBulk(arg0);
                }
            }).on("geocode_progress", new Listener() {
                @Override
                public void call(Object... arg0) {
                    onGeocodeProgress(arg0);
                }
            }).on("error", new Listener() {
                @Override
                public void call(Object... arg0) {
                    LOG.warn("Geocode socket error: {}", arg0.length > 0 ? Sanitizer.forLog(arg0[0]) : "");
                }
            });
            geocoderWebSocket.connect();
        } catch (URISyntaxException ex) {
            LOG.error("Invalid geocoder URL: {}", Sanitizer.forLog(ex.getMessage()));
        }
    }

    /** Stops listening for addresses and closes the connection. Calling it before the geocoder started does nothing. */
    public synchronized void closeGeoCodeSocketIO() {
        if (geocoderWebSocket != null) {
            geocoderWebSocket.off("address");
            geocoderWebSocket.off("addresses_bulk");
            geocoderWebSocket.emit("disconnect");
            geocoderWebSocket.disconnect();
            geocoderWebSocket.close();
        }
    }

    /**
     * Asks the geocoder to geocode the stored addresses, once per job. The socket connects again after a dropped
     * connection and fires its connect event again; a second request could make the validator geocode the batch
     * again and send addresses that have already arrived. The storage ID selects the addresses, so it is not logged.
     */
    private synchronized void emitTemporaryAddressesStorageID() {
        if (geocodeSent) {
            LOG.info("Connected again to the WebSocket Geocoder; the geocode request is not sent twice");
            return;
        }
        geocodeSent = true;
        LOG.debug("Asking the geocoder to geocode the stored addresses");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temporary_addresses_storage_id", this.temporaryAddressesStorageID);
        try {
            JSONObject obj = new JSONObject(gson.toJson(parameters));
            geocoderWebSocket.emit("geocode", obj);
        } catch (JSONException ex) {
            LOG.error("Could not build the geocode message: {}", Sanitizer.forLog(ex.getMessage()));
        }
    }

    /**
     * Connects to the websocket geocoder and asks it to geocode a stored batch; the addresses then arrive in the
     * background. A geocoder runs one job: create a new one for another batch.
     *
     * @param storageId the ID of the stored addresses ({@code temporary_addresses_storage_id})
     * @param addressCount the number of stored addresses
     * @throws IllegalArgumentException if {@code storageId} is {@code null} or empty
     * @throws IllegalStateException if this geocoder has already started a job
     */
    public synchronized void startGeocoding(String storageId, int addressCount) {
        if (storageId == null || storageId.isEmpty()) {
            throw new IllegalArgumentException("The storage ID is required");
        }
        if (started) {
            throw new IllegalStateException(
                    "This geocoder has already started a job; create a new GeocoderWebSockets for another batch");
        }
        this.started = true;
        this.totalAddresses = addressCount;
        // One chunk per hundredth of the job, from 10 to 200 addresses, and as many chunks
        // as fill a stage of 100 addresses (the same rule as the C# SDK). Floating-point
        // division: with integer division the chunk was always 100.
        this.chunkSize = Math.round(Math.min(MAX_CHUNK_SIZE, Math.max(MIN_CHUNK_SIZE, addressCount / 100.0)));
        this.chunksLimit = (long) Math.ceil(BUFFER_FAIL_SAFE_MAX_ADDRESSES / (double) this.chunkSize);
        this.maxAddressesToBeDownloaded = this.chunkSize * this.chunksLimit;
        this.temporaryAddressesStorageID = storageId;
        // A geocoder runs one job, so its socket does not exist yet: the connect event sends the request.
        connectGeocoder();
    }

    /**
     * Tells whether {@link #startGeocoding(String, int)} has started a job. {@link GeocodingManager#websocketsGeocoder}
     * returns a geocoder that has not started when the upload failed.
     *
     * @return {@code true} once a job has started
     */
    public synchronized boolean isStarted() {
        return started;
    }

    /**
     * Tells whether every address of the batch has arrived. A geocoder that has not started has an empty batch, so
     * this is {@code true} for it too: check {@link #isStarted()} first.
     *
     * @return {@code true} when the number of addresses received equals the size of the batch
     */
    public synchronized Boolean isCompleted() {
        return totalAddresses == addressesCount;
    }

    /**
     * Describes the progress, for display.
     *
     * @return a text such as {@code Addresses Processed: 120}
     */
    public synchronized String getProgress() {
        return "Addresses Processed: " + addressesCount;
    }

    /**
     * Returns the addresses received so far, in the order they arrived. Entries that the geocoder sent without an
     * address are left out, so the list can be shorter than {@link #getAddressesCount()}.
     *
     * @return a copy of the geocoded addresses; empty before {@link #startGeocoding(String, int)}
     */
    public synchronized List<Address> getAddresses() {
        return new ArrayList<>(geocodedAddresses);
    }

    /**
     * Returns how many addresses have arrived.
     *
     * @return the number of geocoded addresses received
     */
    public synchronized int getAddressesCount() {
        return addressesCount;
    }

    /**
     * Returns the size of the batch.
     *
     * @return the number of addresses to geocode
     */
    public synchronized int getTotalAddresses() {
        return totalAddresses;
    }

}
