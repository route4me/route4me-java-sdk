package com.route4me.sdk;

import com.route4me.sdk.http.HttpDeleteWithBody;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

public class RequestMethodTest {

    private static final URI TEST_URI = URI.create("https://api.route4me.com/test");

    @Test
    public void testGetCreatesHttpGet() {
        HttpRequestBase request = RequestMethod.GET.create(TEST_URI);
        assertTrue(request instanceof HttpGet);
    }

    @Test
    public void testPostCreatesHttpPost() {
        HttpRequestBase request = RequestMethod.POST.create(TEST_URI);
        assertTrue(request instanceof HttpPost);
    }

    @Test
    public void testPutCreatesHttpPut() {
        HttpRequestBase request = RequestMethod.PUT.create(TEST_URI);
        assertTrue(request instanceof HttpPut);
    }

    @Test
    public void testDeleteCreatesHttpDeleteWithBody() {
        HttpRequestBase request = RequestMethod.DELETE.create(TEST_URI);
        assertTrue(request instanceof HttpDeleteWithBody);
    }

    @Test
    public void testCreatedRequestPreservesURI() {
        for (RequestMethod method : RequestMethod.values()) {
            HttpRequestBase request = method.create(TEST_URI);
            assertEquals(TEST_URI, request.getURI());
        }
    }
}
