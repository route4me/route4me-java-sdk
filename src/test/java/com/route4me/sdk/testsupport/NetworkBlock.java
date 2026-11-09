package com.route4me.sdk.testsupport;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Makes the unit tests fail closed: a test that opens a network connection to any host
 * other than the loopback address fails with an {@link AssertionError} before the
 * connection is made.
 *
 * <p>The block is a default {@link ProxySelector}. The JDK consults it for every plain
 * {@code java.net.Socket} it connects (on Java 8 and on Java 13 and later alike), and so do
 * Apache HttpClient, through those sockets, and OkHttp, which the Socket.IO geocoder uses.
 * So a manager that a test forgot to pass to {@link MockHttp#install}, or the WebSocket
 * geocoder, cannot reach the Route4Me API or any other host. Loopback connections stay
 * possible, for the tests that run a local {@code HttpServer}.
 *
 * <p>A {@code SocketImplFactory} would block every socket too, but it cannot make the
 * loopback exception: once a factory is set, the JDK offers no public way to create its
 * own socket implementation for the sockets that should still connect.
 *
 * <p>{@link DeprecatedEndpointReport}, the surefire run listener, installs the block when
 * the test run starts, and {@link MockHttp} installs it again for every test it wraps,
 * so that a test run from an IDE is covered as well.
 */
public final class NetworkBlock extends ProxySelector {

    /** The start of the message of the {@link AssertionError} a blocked connection throws. */
    public static final String MESSAGE =
            "Unit tests must not open network connections; pass the manager to MockHttp.install(...)";

    private NetworkBlock() {
    }

    /**
     * Installs the block as the default proxy selector of the JVM, unless it is installed
     * already.
     */
    public static synchronized void install() {
        if (!(ProxySelector.getDefault() instanceof NetworkBlock)) {
            ProxySelector.setDefault(new NetworkBlock());
        }
    }

    /**
     * Tells whether the block is the default proxy selector of the JVM.
     *
     * @return {@code true} once {@link #install()} ran and nothing replaced it
     */
    public static boolean isInstalled() {
        return ProxySelector.getDefault() instanceof NetworkBlock;
    }

    /**
     * Lets a connection to the loopback address through, and fails any other.
     *
     * @param uri the target, such as {@code socket://api.route4me.com:443}
     * @return a direct connection, for a loopback target
     * @throws AssertionError for any other target; the message names its scheme, host and
     *     port only, never its query
     */
    @Override
    public List<Proxy> select(URI uri) {
        if (uri != null && isLoopback(uri.getHost())) {
            return Collections.singletonList(Proxy.NO_PROXY);
        }
        String target = uri == null ? "null" : uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort();
        throw new AssertionError(MESSAGE + " (tried " + target + ")");
    }

    /**
     * Does nothing: a failed loopback connection is reported by the socket itself.
     *
     * @param uri     the target
     * @param address the proxy address
     * @param failure the failure
     */
    @Override
    public void connectFailed(URI uri, SocketAddress address, IOException failure) {
        // nothing to record
    }

    /**
     * Returns whether a host string is {@code localhost} or a loopback literal, without any DNS
     * lookup. A name that only starts like one, such as {@code 127.0.0.1.example.net}, is not:
     * the JVM would resolve it and connect to whatever it points at.
     */
    static boolean isLoopback(String host) {
        if (host == null) {
            return false;
        }
        String name = host.toLowerCase(Locale.ROOT);
        if (name.startsWith("[") && name.endsWith("]")) {
            name = name.substring(1, name.length() - 1);
        }
        return "localhost".equals(name) || isIpv4Loopback(name) || "::1".equals(name)
                || "0:0:0:0:0:0:0:1".equals(name);
    }

    /** Returns whether a text is a dotted-quad IPv4 literal in {@code 127.0.0.0/8}, such as {@code 127.0.0.1}. */
    private static boolean isIpv4Loopback(String name) {
        String[] octets = name.split("\\.", -1);
        if (octets.length != 4 || !"127".equals(octets[0])) {
            return false;
        }
        for (String octet : octets) {
            if (!octet.matches("[0-9]{1,3}") || Integer.parseInt(octet) > 255) {
                return false;
            }
        }
        return true;
    }
}
