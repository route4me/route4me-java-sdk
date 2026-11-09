
package com.route4me.sdk.http;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * An HTTP {@code DELETE} request that can carry a body, which several Route4Me endpoints read (Apache's
 * {@code HttpDelete} cannot).
 */
public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

    /** The method name, {@code DELETE}. */
    public static final String METHOD_NAME = "DELETE";

    /**
     * Returns the method name.
     *
     * @return {@link #METHOD_NAME}
     */
    public String getMethod() {
        return METHOD_NAME;
    }

    // setURI only stores the URI; Apache's own HttpDelete and HttpPut call it from their
    // constructors in the same way.
    /**
     * Creates a request to a URL given as text.
     *
     * @param uri the URL
     * @throws IllegalArgumentException if the text is not a valid URI
     */
    @SuppressWarnings("this-escape")
    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    // setURI only stores the URI; Apache's own HttpDelete and HttpPut call it from their
    // constructors in the same way.
    /**
     * Creates a request to a URL.
     *
     * @param uri the URL
     */
    @SuppressWarnings("this-escape")
    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    /** Creates a request without a URL; set one with {@code setURI}. */
    public HttpDeleteWithBody() {
        super();
    }
}
