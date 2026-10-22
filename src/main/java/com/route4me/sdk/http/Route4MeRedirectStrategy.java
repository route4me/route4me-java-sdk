package com.route4me.sdk.http;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectException;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

/**
 * The redirect policy of the SDK's HTTP clients: a redirect is followed only when it
 * leads, over https, to one of the Route4Me API hosts in {@link #ALLOWED_HOSTS}.
 *
 * <p>A redirect keeps the request's query string and headers, and with them the API key
 * (Apache HttpClient copies the {@code Authorization} header too). Following one to another
 * host, or to plain http, would hand the key to whoever runs it. The allowed hosts are the
 * only two that receive the key, {@code api.route4me.com} and {@code wh.route4me.com}; the
 * other Route4Me hosts, such as {@code rapid.route4me.com} and {@code validator.route4me.com},
 * are refused as well.
 * Such a redirect fails the request with a {@link RedirectException} instead, which the
 * SDK reports as an {@link com.route4me.sdk.exception.APIException} with that cause.
 *
 * <p>Which redirects are followed at all is unchanged from Apache HttpClient's
 * {@link DefaultRedirectStrategy}: {@code GET} and {@code HEAD} on 301, 302, 307 and 308,
 * and any method on 303.
 */
public class Route4MeRedirectStrategy extends DefaultRedirectStrategy {

    /** The hosts a redirect may lead to: the two hosts that receive the API key. */
    public static final List<String> ALLOWED_HOSTS = Collections.unmodifiableList(Arrays.asList(
            "api.route4me.com", "wh.route4me.com"));

    private static final String HTTPS = "https";
    private static final int HTTPS_PORT = 443;

    /**
     * Resolves the target of a redirect and checks it against the policy.
     *
     * @param request  the request that was redirected
     * @param response the redirect response
     * @param context  the execution context
     * @return the absolute target of the redirect
     * @throws ProtocolException a {@link RedirectException} when the target is not an
     *                           https URL on an allowed host, or another protocol error
     *                           when the {@code Location} header is missing or invalid
     */
    @Override
    public URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context)
            throws ProtocolException {
        URI location = super.getLocationURI(request, response, context);
        if (!isAllowed(location)) {
            throw new RedirectException("Refused to follow a redirect to " + describe(location)
                    + ": the SDK follows redirects only over https to " + ALLOWED_HOSTS);
        }
        return location;
    }

    /**
     * Tells whether a redirect target is allowed: an absolute https URL on the default port
     * of a host in {@link #ALLOWED_HOSTS}, without user information.
     *
     * @param location the redirect target
     * @return {@code true} when the SDK may follow it
     */
    public static boolean isAllowed(URI location) {
        if (location == null || !location.isAbsolute() || location.getHost() == null) {
            return false;
        }
        if (!HTTPS.equalsIgnoreCase(location.getScheme()) || location.getRawUserInfo() != null) {
            return false;
        }
        int port = location.getPort();
        if (port != -1 && port != HTTPS_PORT) {
            return false;
        }
        return ALLOWED_HOSTS.contains(location.getHost().toLowerCase(Locale.ROOT));
    }

    /** Describes a target by scheme and host only: its query may echo the API key. */
    private static String describe(URI location) {
        return location.getScheme() + "://" + location.getHost()
                + (location.getPort() == -1 ? "" : ":" + location.getPort());
    }
}
