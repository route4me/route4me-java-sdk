
package com.route4me.sdk.http;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 *
 * @author juan
 */
public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "DELETE";

    public String getMethod() {
        return METHOD_NAME;
    }

    // setURI only stores the URI; Apache's own HttpDelete and HttpPut call it from their
    // constructors in the same way.
    @SuppressWarnings("this-escape")
    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    // setURI only stores the URI; Apache's own HttpDelete and HttpPut call it from their
    // constructors in the same way.
    @SuppressWarnings("this-escape")
    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteWithBody() {
        super();
    }
}
