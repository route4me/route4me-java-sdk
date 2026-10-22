package com.route4me.sdk.services.geocoding;

import io.socket.client.Socket;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

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
