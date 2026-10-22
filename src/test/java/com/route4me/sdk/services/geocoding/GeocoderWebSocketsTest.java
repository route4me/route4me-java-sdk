package com.route4me.sdk.services.geocoding;

import io.socket.client.Socket;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link GeocoderWebSockets} that never open a connection: the socket is a
 * Mockito mock, put in place with reflection.
 */
public class GeocoderWebSocketsTest {

    private final Socket socket = Mockito.mock(Socket.class);

    /** Clears the socket field, in case a test left a mock in it. */
    @After
    public void clearSocket() throws Exception {
        setSocket(new GeocoderWebSockets(), null);
    }

    /** Puts a socket into the {@code geocoderWebSocket} field of a geocoder. */
    private static void setSocket(GeocoderWebSockets geocoder, Socket socket) throws Exception {
        Field field = GeocoderWebSockets.class.getDeclaredField("geocoderWebSocket");
        field.setAccessible(true);
        field.set(geocoder, socket);
    }

    /**
     * Starts a geocoding job of {@code totalAddresses} on a connected mock socket, asks for
     * the first download and returns the {@code download} payload it emits.
     */
    private JSONObject firstDownload(int totalAddresses) throws Exception {
        Socket connected = Mockito.mock(Socket.class);
        Mockito.when(connected.connected()).thenReturn(true);
        GeocoderWebSockets geocoder = new GeocoderWebSockets();
        setSocket(geocoder, connected);
        geocoder.startGeocoding("TMP-1", totalAddresses);

        Method download = GeocoderWebSockets.class.getDeclaredMethod("downloadAddresses", int.class);
        download.setAccessible(true);
        download.invoke(geocoder, 0);

        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(connected).emit(Mockito.eq("download"), payload.capture());
        verify(connected, never()).connect();
        return (JSONObject) payload.getValue();
    }

    @Test
    public void downloadChunksFollowTheNumberOfAddresses() throws Exception {
        // chunk_size = round(min(200, max(10, total / 100))); chunks_limit = ceil(100 / chunk_size)
        int[][] cases = {{50, 10, 10}, {500, 10, 10}, {1500, 15, 7}, {5000, 50, 2}, {15050, 151, 1}, {50000, 200, 1}};
        for (int[] expected : cases) {
            JSONObject payload = firstDownload(expected[0]);

            assertEquals(expected[0] + " addresses", expected[1], payload.getInt("chunk_size"));
            assertEquals(expected[0] + " addresses", expected[2], payload.getInt("chunks_limit"));
            assertEquals("TMP-1", payload.getString("temporary_addresses_storage_id"));
            assertEquals(0, payload.getInt("from_index"));
        }
    }

    @Test
    public void eachGeocoderHasItsOwnSocket() throws Exception {
        GeocoderWebSockets first = new GeocoderWebSockets();
        GeocoderWebSockets second = new GeocoderWebSockets();
        setSocket(first, socket);

        second.closeGeoCodeSocketIO();

        Mockito.verifyNoInteractions(socket);
        Field field = GeocoderWebSockets.class.getDeclaredField("geocoderWebSocket");
        assertFalse("the socket field must not be static", Modifier.isStatic(field.getModifiers()));
        assertFalse("the socket field must not be public", Modifier.isPublic(field.getModifiers()));
    }

    @Test
    public void closeUnsubscribesTheEventsItListensTo() throws Exception {
        GeocoderWebSockets geocoder = new GeocoderWebSockets();
        setSocket(geocoder, socket);

        geocoder.closeGeoCodeSocketIO();

        verify(socket).off("address");
        verify(socket).off("addresses_bulk");
        verify(socket, never()).off("address_bulk");
        verify(socket).disconnect();
        verify(socket).close();
        verify(socket, never()).off(anyString(), Mockito.any());
    }
}
