package com.route4me.sdk;

import com.route4me.sdk.http.HttpDeleteWithBody;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

public enum RequestMethod {

    GET(HttpGet.class), POST(HttpPost.class), DELETE(HttpDeleteWithBody.class), PUT(HttpPut.class);

    private final Class<? extends HttpRequestBase> clazz;

    RequestMethod(Class<? extends HttpRequestBase> clazz) {
        this.clazz = clazz;
    }

    public HttpRequestBase create(URI uri) {
        try {
            return this.clazz.getConstructor(URI.class).newInstance(uri);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //(almost) unreachable path
            throw new RuntimeException(ex);
        }
    }
}
