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

    private final long bufferFailSafeMaxAddresses = 100;
    private final long chunkSize = Math.round(Math.min(200, Math.max(100, this.totalAddresses / 100)));
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
                geocoderWebSocket = IO.socket(GEOCODER_URL);
                geocoderWebSocket.on(Socket.EVENT_CONNECT, new Listener() {
                    @Override
                    public void call(Object... os) {
                        LOG.info("Connected to the WebSocket Geocoder");
                    }

                }).on(Socket.EVENT_DISCONNECT, new Listener() {

                    @Override
                    public void call(Object... args) {
                        LOG.info("Now Disconnected from the WebSocket Geocoder");
                    }

                }).on("connect_error", new Listener() {

                    @Override
                    public void call(Object... arg0) {
                        LOG.warning("Geocode socket connection error");

                    }
                }).on("connect_timeout", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        LOG.warning("Geocode socket connection error");
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
                        List<Address> addressesChunk = gson.fromJson(arg0[0].toString(), List.class);
                        geocodedAddresses.addAll(addressesChunk);
                        addressesCount += addressesChunk.size();
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
                        Map data = gson.fromJson(arg0[0].toString(), Map.class);
                        if (Objects.equals(data.get("total"), data.get("done"))){
                            downloadAddresses(0);
                        }
                    }
                }).on("error", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        LOG.info(String.valueOf(arg0.length));
                        LOG.info(arg0[0].toString());
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
        if (geocoderWebSocket != null && geocoderWebSocket.connected()) {
            geocoderWebSocket.emit("disconnect", temporaryAddressesStorageID);
            geocoderWebSocket.disconnect();
            geocoderWebSocket.off("address");
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
        parameters.put("force_restart", true);
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
