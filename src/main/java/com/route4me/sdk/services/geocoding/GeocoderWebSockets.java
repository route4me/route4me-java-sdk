/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.Gson;
import com.route4me.sdk.services.routing.Address;
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

public class GeocoderWebSockets {

    private final static Logger LOG = Logger.getLogger(GeocoderWebSockets.class.getName());

    public Socket geocoderWebSocket;

    private static final String GEOCODER_URL = "https://validator.route4me.com";

    private List<Address> geocodedAddresses;
    protected Gson gson = new Gson();

    private int addressesCount = 0;
    private int totalAddresses = 0;
    private long nextDownloadStage = 0;
    private String temporaryAddressesStorageID;

    private final long bufferFailSafeMaxAddresses = 500;
    private final long chunkSize = Math.round(Math.min(500, Math.max(500, this.totalAddresses / 100)));
    private final long chunksLimit = Math.round(Math.ceil(bufferFailSafeMaxAddresses / chunkSize));
    private final long maxAddressesToBeDownloaded = chunkSize * chunksLimit;
    
    
    public GeocoderWebSockets() {
        this.geocodedAddresses = null;
    }

    private Address addressParser(String data) {
        WebSocketsAddress webSocketAddress;
        webSocketAddress = gson.fromJson(data, WebSocketsAddress.class);
        return webSocketAddress.getAddress();
    }

    private void addressesParser(WebSocketsAddress[] addressesChunk) {
        for (WebSocketsAddress a: addressesChunk){
            geocodedAddresses.add(a.getAddress());
        }
    }    
    
    private void downloadAddresses(int start) {
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
                IO.Options opts = new IO.Options();
                opts.forceNew = true;
                opts.reconnection = true;
                opts.reconnectionDelay = 20;
                opts.reconnectionDelayMax = 60;
                opts.timeout = 600;
                geocoderWebSocket = IO.socket(GEOCODER_URL, opts);
                geocoderWebSocket.on(Socket.EVENT_CONNECT, new Listener() {
                    @Override
                    public void call(Object... os) {
                        LOG.info("Connected to the WebSocket Geocoder");
                        if (os.length > 0) {
                            LOG.fine(os[0].toString());
                        }
                    }

                }).on(Socket.EVENT_DISCONNECT, new Listener() {

                    @Override
                    public void call(Object... args) {
                        LOG.info("Now Disconnected from the WebSocket Geocoder");
                        if (args.length > 0) {
                            LOG.fine(args[0].toString());
                        }
                    }

                }).on("connect_error", new Listener() {

                    @Override
                    public void call(Object... arg0) {
                        LOG.warning("Geocode socket connection error");
                        if (arg0.length > 0) {
                            LOG.warning(arg0[0].toString());
                        }
                    }
                }).on("connect_timeout", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        LOG.warning("Geocode socket connection timeout error");
                        if (arg0.length > 0) {
                            LOG.warning(arg0[0].toString());
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
                        WebSocketsAddress[] addressesChunk = gson.fromJson(arg0[0].toString(), WebSocketsAddress[].class);
                        addressesParser(addressesChunk);
                        addressesCount += addressesChunk.length;
                        LOG.log(Level.FINE, "Addresses Received: {0}", addressesChunk.length);
			if (addressesCount == nextDownloadStage) {
				downloadAddresses(addressesCount);
			}
                        if (addressesCount == totalAddresses){
                            closeGeoCodeSocketIO();
                        }
                    }
                }).on("geocode_progress", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        LOG.fine(arg0[0].toString());
                        Map data = gson.fromJson(arg0[0].toString(), Map.class);
                        if (Objects.equals(data.get("total"), data.get("done"))){
                            downloadAddresses(0);
                        }
                    }
                }).on("error", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        LOG.warning(String.valueOf(arg0.length));
                        LOG.warning(arg0[0].toString());
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

    public void closeGeoCodeSocketIO() {
        if (geocoderWebSocket != null) {
            geocoderWebSocket.off("address");
            geocoderWebSocket.off("address_bulk");
            geocoderWebSocket.emit("disconnect");
            geocoderWebSocket.disconnect();
            geocoderWebSocket.close();
        }
    }

    public void startGeocodingAltenative(String temporaryAddressesStorageID, int totalAddresses) {
        this.totalAddresses = totalAddresses;
        this.temporaryAddressesStorageID = temporaryAddressesStorageID;
        geocodedAddresses = new ArrayList<>();
        if (geocoderWebSocket == null || !geocoderWebSocket.connected()) {
            connectGeocoder();
        }
        geocoderWebSocket.emit("setopid", temporaryAddressesStorageID);
    }

    public void startGeocoding(String temporaryAddressesStorageID, int totalAddresses) {
        this.totalAddresses = totalAddresses;
        this.temporaryAddressesStorageID = temporaryAddressesStorageID;
        geocodedAddresses = new ArrayList<>();
        if (geocoderWebSocket == null || !geocoderWebSocket.connected()) {
            connectGeocoder();
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temporary_addresses_storage_id", this.temporaryAddressesStorageID);
        try {
            JSONObject obj = new JSONObject(gson.toJson(parameters));
            geocoderWebSocket.emit("geocode", obj);
        } catch (JSONException ex) {
            Logger.getLogger(GeocoderWebSockets.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Boolean isCompleted() {
        return getTotalAddresses() == getAddressesCount();
    }

    public String getProgress() {
        return "Addresses Processed: " + addressesCount;
    }

    /**
     * @return the geocodedAddresses
     */
    public List<Address> getAddresses() {
        return geocodedAddresses;
    }

    /**
     * @return the addressesCount
     */
    public int getAddressesCount() {
        return addressesCount;
    }

    /**
     * @return the totalAddresses
     */
    public int getTotalAddresses() {
        return totalAddresses;
    }

}
