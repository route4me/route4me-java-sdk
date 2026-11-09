package com.route4me.sdk.services.geocoding;

import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.testsupport.Log4jCapture;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link GeocoderWebSockets} that never open a connection: the socket is a
 * Mockito mock, put in place with reflection or returned by {@link GeocoderWebSockets#newSocket()}.
 */
public class GeocoderWebSocketsTest {

    private static final String STORAGE_ID = "TMP-STORAGE-7f3a";

    private final Socket socket = Mockito.mock(Socket.class);

    /** A geocoder whose socket is a mock that records its listeners and never connects. */
    private static final class OfflineGeocoder extends GeocoderWebSockets {
        private final Socket mock = Mockito.mock(Socket.class, Mockito.RETURNS_SELF);

        @Override
        Socket newSocket() {
            return mock;
        }

        /** Returns the listener the geocoder registered for {@code event}. */
        Listener listener(String event) {
            ArgumentCaptor<String> events = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Listener> listeners = ArgumentCaptor.forClass(Listener.class);
            verify(mock, Mockito.atLeastOnce()).on(events.capture(), listeners.capture());
            Map<String, Listener> byEvent = new HashMap<>();
            for (int i = 0; i < events.getAllValues().size(); i++) {
                byEvent.put(events.getAllValues().get(i), listeners.getAllValues().get(i));
            }
            Listener listener = byEvent.get(event);
            assertNotNull("no listener for " + event, listener);
            return listener;
        }
    }

    /** Puts a socket into the {@code geocoderWebSocket} field of a geocoder. */
    private static void setSocket(GeocoderWebSockets geocoder, Socket socket) throws Exception {
        Field field = GeocoderWebSockets.class.getDeclaredField("geocoderWebSocket");
        field.setAccessible(true);
        field.set(geocoder, socket);
    }

    /** Builds an {@code addresses_bulk} payload with one entry per address. */
    private static JSONArray chunk(String... addresses) throws Exception {
        JSONArray array = new JSONArray();
        for (String address : addresses) {
            array.put(new JSONObject().put("address", new JSONObject().put("address", address).put("lat", 1.5)
                    .put("lng", -2.5)));
        }
        return array;
    }

    /**
     * Starts a geocoding job of {@code totalAddresses} on a mock socket, asks for the first
     * download once it is connected and returns the {@code download} payload it emits.
     */
    private JSONObject firstDownload(int totalAddresses) throws Exception {
        OfflineGeocoder geocoder = new OfflineGeocoder();
        geocoder.startGeocoding("TMP-1", totalAddresses);
        Mockito.when(geocoder.mock.connected()).thenReturn(true);

        Method download = GeocoderWebSockets.class.getDeclaredMethod("downloadAddresses", int.class);
        download.setAccessible(true);
        download.invoke(geocoder, 0);

        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(geocoder.mock).emit(Mockito.eq("download"), payload.capture());
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

    /** Regression: a geocode request lost with a dropped connection was never sent again, and the job hung. */
    @Test
    public void aReconnectBeforeAnyAddressSendsTheGeocodeRequestAgain() throws Exception {
        OfflineGeocoder geocoder = new OfflineGeocoder();
        geocoder.startGeocoding(STORAGE_ID, 3);
        verify(geocoder.mock, never()).emit(Mockito.eq("geocode"), Mockito.any());
        Listener connect = geocoder.listener(Socket.EVENT_CONNECT);

        connect.call();
        // Socket.IO connects again after a dropped connection and fires the connect event again.
        geocoder.listener(Socket.EVENT_DISCONNECT).call("transport close");
        // A message without a payload is ignored, so it does not count as an address.
        geocoder.listener("address").call();
        connect.call();

        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(geocoder.mock, Mockito.times(2)).emit(Mockito.eq("geocode"), payload.capture());
        for (Object sent : payload.getAllValues()) {
            assertEquals(STORAGE_ID, ((JSONObject) sent).getString("temporary_addresses_storage_id"));
        }
        verify(geocoder.mock, Mockito.times(1)).connect();
    }

    /** Regression: every reconnect sent the geocode request again, so addresses that had arrived came twice. */
    @Test
    public void aReconnectAfterAnAddressArrivedDoesNotSendTheGeocodeRequestAgain() throws Exception {
        for (String event : new String[] {"address", "addresses_bulk"}) {
            OfflineGeocoder geocoder = new OfflineGeocoder();
            geocoder.startGeocoding(STORAGE_ID, 3);
            Listener connect = geocoder.listener(Socket.EVENT_CONNECT);

            connect.call();
            Object message = "address".equals(event) ? chunk("1 Main St").get(0) : chunk("1 Main St");
            geocoder.listener(event).call(message);
            geocoder.listener(Socket.EVENT_DISCONNECT).call("transport close");
            connect.call();
            connect.call();

            verify(geocoder.mock, Mockito.times(1)).emit(Mockito.eq("geocode"), Mockito.any());
            assertEquals(event, 1, geocoder.getAddressesCount());
            assertFalse(event, geocoder.isCompleted());
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

    @Test
    public void aGeocoderThatHasNotStartedSaysSo() {
        GeocoderWebSockets geocoder = new GeocoderWebSockets();

        assertFalse(geocoder.isStarted());
        assertTrue(geocoder.getAddresses().isEmpty());
        assertTrue("an empty batch is complete", geocoder.isCompleted());
        assertEquals("Addresses Processed: 0", geocoder.getProgress());
    }

    @Test
    public void startConnectsAndAsksForTheBatchWithoutLoggingItsStorageId() throws Exception {
        OfflineGeocoder geocoder = new OfflineGeocoder();
        Log4jCapture log = Log4jCapture.start();
        try {
            geocoder.startGeocoding(STORAGE_ID, 2);
            verify(geocoder.mock).connect();
            assertTrue(geocoder.isStarted());

            geocoder.listener(Socket.EVENT_CONNECT).call("connected");
            geocoder.listener(Socket.EVENT_DISCONNECT).call("transport close");
            geocoder.listener("connect_error").call("refused");
            geocoder.listener("connect_timeout").call();
            geocoder.listener("error").call("boom");
        } finally {
            log.stop();
        }

        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(geocoder.mock).emit(Mockito.eq("geocode"), payload.capture());
        assertEquals(STORAGE_ID, ((JSONObject) payload.getValue()).getString("temporary_addresses_storage_id"));
        assertFalse(log.events().isEmpty());
        for (Log4jCapture.Event event : log.events()) {
            assertFalse("the storage ID selects the addresses; it must not be logged: " + event.getMessage(),
                    String.valueOf(event.getMessage()).contains(STORAGE_ID));
        }
    }

    @Test
    public void aSecondJobOnTheSameGeocoderIsRefused() {
        OfflineGeocoder geocoder = new OfflineGeocoder();
        geocoder.startGeocoding(STORAGE_ID, 2);

        try {
            geocoder.startGeocoding("TMP-OTHER", 5);
            fail("a second job must be refused");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("new GeocoderWebSockets"));
        }
        assertEquals("the first job is kept", 2, geocoder.getTotalAddresses());
        verify(geocoder.mock).connect();
    }

    @Test
    public void aStorageIdIsRequired() {
        // An offline geocoder: if the check regressed, a real one would connect to the live validator.
        OfflineGeocoder geocoder = new OfflineGeocoder();
        for (String storageId : new String[] {null, ""}) {
            try {
                geocoder.startGeocoding(storageId, 2);
                fail("a missing storage ID must be refused");
            } catch (IllegalArgumentException expected) {
                assertFalse(geocoder.isStarted());
            }
        }
        verify(geocoder.mock, never()).connect();
    }

    @Test
    public void bulkAddressesAreCollectedAndTheLastOneClosesTheSocket() throws Exception {
        OfflineGeocoder geocoder = new OfflineGeocoder();
        geocoder.startGeocoding(STORAGE_ID, 3);
        Listener bulk = geocoder.listener("addresses_bulk");

        bulk.call(chunk("1 Main St", "2 Main St"));
        assertFalse(geocoder.isCompleted());
        verify(geocoder.mock, never()).close();
        bulk.call(chunk("3 Main St"));

        assertTrue(geocoder.isCompleted());
        assertEquals(3, geocoder.getAddressesCount());
        List<Address> addresses = geocoder.getAddresses();
        assertEquals("1 Main St", addresses.get(0).getAddress());
        assertEquals("3 Main St", addresses.get(2).getAddress());
        assertEquals(1.5, addresses.get(0).getLatitude(), 0.0);
        verify(geocoder.mock).close();
        assertEquals("Addresses Processed: 3", geocoder.getProgress());
    }

    @Test
    public void singleAddressesAreCollected() throws Exception {
        OfflineGeocoder geocoder = new OfflineGeocoder();
        geocoder.startGeocoding(STORAGE_ID, 1);

        geocoder.listener("address").call(chunk("9 Side Rd").get(0));

        assertTrue(geocoder.isCompleted());
        assertEquals("9 Side Rd", geocoder.getAddresses().get(0).getAddress());
        verify(geocoder.mock).close();
    }

    @Test
    public void malformedMessagesAreIgnored() throws Exception {
        OfflineGeocoder geocoder = new OfflineGeocoder();
        geocoder.startGeocoding(STORAGE_ID, 4);
        Listener bulk = geocoder.listener("addresses_bulk");
        Listener single = geocoder.listener("address");
        Listener progress = geocoder.listener("geocode_progress");

        for (Listener listener : new Listener[] {bulk, progress}) {
            listener.call();
            listener.call((Object) null);
            listener.call(JSONObject.NULL);
            listener.call("{not json");
            listener.call("\"a text\"");
        }
        single.call();
        single.call((Object) null);
        progress.call(new JSONObject());

        assertEquals(0, geocoder.getAddressesCount());
        assertTrue(geocoder.getAddresses().isEmpty());
        verify(geocoder.mock, never()).emit(Mockito.eq("download"), Mockito.any());

        // A null entry and an entry without an address count as received, so the next
        // download starts after them, but they are not in the list.
        JSONArray withGaps = chunk("1 Main St");
        withGaps.put(JSONObject.NULL);
        withGaps.put(new JSONObject());
        withGaps.put(chunk("4 Main St").get(0));
        bulk.call(withGaps);

        assertTrue(geocoder.isCompleted());
        assertEquals(4, geocoder.getAddressesCount());
        assertEquals(2, geocoder.getAddresses().size());
    }

    @Test
    public void anAddressMessageWithoutAnAddressCountsSoTheJobCompletes() throws Exception {
        OfflineGeocoder geocoder = new OfflineGeocoder();
        geocoder.startGeocoding(STORAGE_ID, 4);
        Listener single = geocoder.listener("address");

        single.call(new JSONObject().put("address", JSONObject.NULL));
        single.call(new JSONObject());
        single.call("{not json");
        assertFalse(geocoder.isCompleted());
        verify(geocoder.mock, never()).close();
        single.call(chunk("9 Side Rd").get(0));

        assertTrue("every address message counts, with or without an address", geocoder.isCompleted());
        assertEquals(4, geocoder.getAddressesCount());
        assertEquals(1, geocoder.getAddresses().size());
        assertEquals("9 Side Rd", geocoder.getAddresses().get(0).getAddress());
        verify(geocoder.mock).close();
    }

    /** Regression: completion needed an exact count, so one message more than the batch kept the job open. */
    @Test
    public void aMessageMoreThanTheBatchStillCompletesTheJob() throws Exception {
        OfflineGeocoder single = new OfflineGeocoder();
        single.startGeocoding(STORAGE_ID, 2);
        Listener address = single.listener("address");

        address.call(chunk("1 Main St").get(0));
        address.call(chunk("2 Main St").get(0));
        assertTrue(single.isCompleted());
        // A duplicate after the last address.
        address.call(chunk("2 Main St").get(0));

        assertTrue("passing the size of the batch completes the job", single.isCompleted());
        assertEquals(3, single.getAddressesCount());
        verify(single.mock, Mockito.atLeastOnce()).close();

        OfflineGeocoder bulk = new OfflineGeocoder();
        bulk.startGeocoding(STORAGE_ID, 2);

        // A chunk that jumps past the size of the batch.
        bulk.listener("addresses_bulk").call(chunk("1 Main St", "2 Main St", "2 Main St"));

        assertTrue(bulk.isCompleted());
        assertEquals(3, bulk.getAddressesCount());
        verify(bulk.mock).close();
    }

    @Test
    public void aFinishedGeocodeStartsTheDownload() throws Exception {
        OfflineGeocoder geocoder = new OfflineGeocoder();
        geocoder.startGeocoding(STORAGE_ID, 2);
        Mockito.when(geocoder.mock.connected()).thenReturn(true);
        Listener progress = geocoder.listener("geocode_progress");

        progress.call(new JSONObject().put("total", 2).put("done", 1));
        verify(geocoder.mock, never()).emit(Mockito.eq("download"), Mockito.any());
        progress.call(new JSONObject().put("total", 2).put("done", 2));

        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(geocoder.mock).emit(Mockito.eq("download"), payload.capture());
        assertEquals(0, ((JSONObject) payload.getValue()).getInt("from_index"));
    }

    @Test
    public void addressesCanBeReadWhileTheyArrive() throws Exception {
        final OfflineGeocoder geocoder = new OfflineGeocoder();
        final int total = 2000;
        geocoder.startGeocoding(STORAGE_ID, total);
        final Listener bulk = geocoder.listener("addresses_bulk");
        final JSONArray one = chunk("1 Main St");
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        final CountDownLatch go = new CountDownLatch(1);
        Thread eventThread = new Thread(() -> {
            try {
                go.await();
                for (int i = 0; i < total; i++) {
                    bulk.call(one);
                }
            } catch (Throwable ex) {
                failure.set(ex);
            }
        });
        eventThread.start();

        go.countDown();
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
        try {
            while (!geocoder.isCompleted() && System.nanoTime() < deadline) {
                int seen = 0;
                for (Address address : geocoder.getAddresses()) {
                    assertNotNull(address);
                    seen++;
                }
                assertTrue(seen <= total);
            }
        } catch (ConcurrentModificationException ex) {
            fail("reading the addresses must not race with the event thread: " + ex);
        }
        eventThread.join(TimeUnit.SECONDS.toMillis(30));

        assertNull(failure.get());
        assertTrue("the caller sees the job complete", geocoder.isCompleted());
        assertEquals(total, geocoder.getAddresses().size());
    }
}
