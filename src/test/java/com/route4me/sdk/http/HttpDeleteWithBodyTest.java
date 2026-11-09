package com.route4me.sdk.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HttpDeleteWithBodyTest {

    @Test
    public void testGetMethodReturnsDelete() {
        HttpDeleteWithBody request = new HttpDeleteWithBody();
        assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testStringConstructorSetsURI() {
        HttpDeleteWithBody request = new HttpDeleteWithBody("https://example.com/test");
        assertEquals(URI.create("https://example.com/test"), request.getURI());
    }

    @Test
    public void testURIConstructorSetsURI() {
        URI uri = URI.create("https://example.com/test");
        HttpDeleteWithBody request = new HttpDeleteWithBody(uri);
        assertEquals(uri, request.getURI());
    }

    @Test
    public void testSupportsEntity() throws UnsupportedEncodingException {
        HttpDeleteWithBody request = new HttpDeleteWithBody("https://example.com/test");
        assertTrue(request instanceof HttpEntityEnclosingRequestBase);
        request.setEntity(new StringEntity("{\"key\":\"value\"}"));
        assertNotNull(request.getEntity());
    }

    @Test
    public void testDefaultConstructor() {
        HttpDeleteWithBody request = new HttpDeleteWithBody();
        assertNull(request.getURI());
        assertEquals("DELETE", request.getMethod());
    }
}
