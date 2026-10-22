package com.route4me.sdk;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Test-only bridge to the package-private HTTP client hook in {@link Manager}.
 *
 * <p>{@link Manager#setHttpClient} is package-private so that it stays out of the
 * public API. This class lives in the same package, in test sources only, so that the
 * helpers in {@code com.route4me.sdk.testsupport} can reach the hook.
 */
public final class ManagerTestAccess {

    /** Not instantiable: static helpers only. */
    private ManagerTestAccess() {
    }

    /**
     * Makes {@code manager} send every request through {@code client}.
     *
     * @param manager the manager under test
     * @param client  the (mocked) client that receives every request
     */
    public static void useHttpClient(Manager manager, final CloseableHttpClient client) {
        manager.setHttpClient(client);
    }

    /**
     * Returns how many connections the pool of {@code manager} may hold.
     *
     * @param manager the manager under test
     * @return the per-route and total maximum of its connection pool
     */
    public static int maxConnections(Manager manager) {
        return manager.maxConnections();
    }

    /**
     * Makes {@code manager} create its own HTTP client again on the next request.
     *
     * @param manager the manager under test
     */
    public static void useDefaultHttpClient(Manager manager) {
        manager.setHttpClient(null);
    }
}
