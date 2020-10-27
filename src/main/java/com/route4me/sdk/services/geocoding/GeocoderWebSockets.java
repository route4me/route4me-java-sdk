/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.route4me.sdk.services.routing.Address;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class GeocoderWebSockets {

    private final static Logger LOG = Logger.getLogger(GeocoderWebSockets.class.getName());

    public static Socket geocoderWebSocket;

    private static final String GEOCODER_URL = "https://validator.route4me.com";

    private List<Address> geocodedAddresses;
    protected Gson gson = new Gson();

    private int addressesCount = 0;
    private int totalAddresses = 0;

    public GeocoderWebSockets() {
        this.geocodedAddresses = null;
    }

    private Address addressParser(String data) {
        WebSocketsAddress webSocketAddress;
        webSocketAddress = gson.fromJson(data, WebSocketsAddress.class);
        return webSocketAddress.getAddress();
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
                            LOG.log(Level.INFO, "Addresses Processed: {0} From {1}", new Object[]{addressesCount, totalAddresses});
                        }
                        if (getTotalAddresses() == getAddressesCount()) {
                            LOG.info("Geocoding Process Finished");
                            closeGeoCodeSocketIO();
                        }
                    }
                }).on("addresses_bulk", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        JSONObject data = (JSONObject) arg0[0];
                        LOG.info(data.toString());
                    }

                }).on("geocode_progress", new Listener() {
                    @Override
                    public void call(Object... arg0) {
                        LOG.info(String.valueOf(arg0.length));
                        LOG.info(arg0[0].toString());
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
    
    private void closeGeoCodeSocketIO() {
        if (geocoderWebSocket != null && geocoderWebSocket.connected()) {
            LOG.info("Closing geocode socket connection");
            geocoderWebSocket.disconnect();
            geocoderWebSocket.off("address");
        }
    }

    public void startGeocoding(String jobID, int totalAddresses) {
        this.totalAddresses = totalAddresses;
        geocodedAddresses = new ArrayList<>();
        if (geocoderWebSocket == null || !geocoderWebSocket.connected()) {
            connectGeocoder();
        }
        JsonObject message = new JsonObject();
        message.addProperty("temporary_addresses_storage_id", jobID);
        message.addProperty("force_restart", true);

        geocoderWebSocket.emit("setopid", jobID);

    }

    public void startGeocoding2(String jobID, int totalAddresses) {
        this.totalAddresses = totalAddresses;
        geocodedAddresses = new ArrayList<>();
        if (geocoderWebSocket == null || !geocoderWebSocket.connected()) {
            connectGeocoder();
        }
        JsonObject message = new JsonObject();
        message.addProperty("temporary_addresses_storage_id", jobID);
        message.addProperty("force_restart", true);
        System.out.println(gson.toJson(message));

        geocoderWebSocket.emit("geocode", gson.toJson(message), new Ack() {
            @Override
            public void call(Object... args) {
                LOG.info(String.valueOf(args.length));
                LOG.info(args[0].toString());

            }
        });

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
