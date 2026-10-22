package com.route4me.sdk;

import com.route4me.sdk.http.HttpDeleteWithBody;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * The HTTP methods the managers send. {@code DELETE} can carry a body, which some
 * Route4Me endpoints expect.
 */
public enum RequestMethod {

    /** {@code GET}. */
    GET(HttpGet.class),
    /** {@code POST}. */
    POST(HttpPost.class),
    /** {@code DELETE}, which may carry a body ({@link HttpDeleteWithBody}). */
    DELETE(HttpDeleteWithBody.class),
    /** {@code PUT}. */
    PUT(HttpPut.class),
    /**
     * {@code PATCH}, for partial updates. It is here for future use: the managers keep
     * sending {@code PUT} where they do today, which the API accepts as well.
     */
    PATCH(HttpPatch.class);

    private final Class<? extends HttpRequestBase> clazz;

    RequestMethod(Class<? extends HttpRequestBase> clazz) {
        this.clazz = clazz;
    }

    /**
     * Creates an Apache HttpClient request of this method.
     *
     * @param uri the request URL
     * @return the request, without headers or body
     * @throws RuntimeException if the request class cannot be created, which does not happen with the classes of this
     *     enum
     */
    public HttpRequestBase create(URI uri) {
        try {
            return this.clazz.getConstructor(URI.class).newInstance(uri);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            //(almost) unreachable path
            throw new RuntimeException(ex);
        }
    }
}
