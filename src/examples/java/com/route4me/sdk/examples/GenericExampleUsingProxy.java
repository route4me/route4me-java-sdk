package com.route4me.sdk.examples;

import com.route4me.sdk.examples.utils.ExampleInput;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.DataObject;
import com.route4me.sdk.services.routing.RoutingManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Lists optimizations through an HTTP proxy: a {@link RoutingManager} built with a proxy host, port and scheme calls
 * {@link RoutingManager#getOptimizations}. Set {@code PROXY_HOST} to the proxy, {@code PROXY_PORT} to its port (1 to
 * 65535) when it is not 8080, and {@code PROXY_SCHEME} to how the example connects to the proxy, {@code http} or
 * {@code https}; it defaults to {@code https}. A port out of range or another scheme stops the example.
 */
public class GenericExampleUsingProxy {

    public static void main(String[] args) {
        String apiKey = System.getenv("R4M_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Set R4M_API_KEY to your Route4Me API key.");
            return;
        }
        String proxyHost = System.getenv("PROXY_HOST");
        if (proxyHost == null || proxyHost.isEmpty()) {
            System.err.println("Set PROXY_HOST to your proxy, PROXY_PORT to its port when it is not 8080, and"
                    + " PROXY_SCHEME to http when the proxy takes plain HTTP.");
            return;
        }
        Integer proxyPort = 8080;
        String port = System.getenv("PROXY_PORT");
        if (port != null && !port.isEmpty()) {
            proxyPort = ExampleInput.toInteger("PROXY_PORT", port);
            if (proxyPort == null) {
                return;
            }
            if (proxyPort < 1 || proxyPort > 65535) {
                System.err.println("PROXY_PORT must be a port from 1 to 65535: " + port);
                return;
            }
        }
        String proxySchema = "https";
        String scheme = System.getenv("PROXY_SCHEME");
        if (scheme != null && !scheme.trim().isEmpty()) {
            proxySchema = scheme.trim().toLowerCase(Locale.ROOT);
            if (!"http".equals(proxySchema) && !"https".equals(proxySchema)) {
                System.err.println("PROXY_SCHEME must be http or https: " + scheme);
                return;
            }
        }
        // A manager holds a pool of HTTP connections: close it when you are done.
        try (RoutingManager manager = new RoutingManager(apiKey, proxyHost, proxyPort, proxySchema)) {
            List<DataObject> responseObject = manager.getOptimizations(0, 5);
            if (responseObject == null || responseObject.isEmpty()) {
                System.out.println("No optimizations found");
                return;
            }
            DataObject opt = responseObject.get(0);
            System.out.println("Optimization ID: " + opt.getOptimizationProblemId());
            if (opt.getAddresses() != null) {
                for (Address address : opt.getAddresses()) {
                    System.out.println(address);
                }
            }
        } catch (APIException | IOException e) {
            //handle exception
            e.printStackTrace();
        }

    }
}
