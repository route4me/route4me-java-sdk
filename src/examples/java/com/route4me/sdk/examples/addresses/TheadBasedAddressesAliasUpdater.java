/*
 * The MIT License
 *
 * Copyright 2023 Route4Me.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.route4me.sdk.examples.addresses;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Renames every address of a route in parallel: each task gives an address a random alias with
 * {@link RoutingManager#updateAddressAttribute}, after the route is read with {@link RoutingManager#getRoute}. Set
 * {@code ROUTE_ID} to a route of a test account: the example replaces the alias of each of its stops.
 */
public class TheadBasedAddressesAliasUpdater implements Callable<Address> {

    private final Address address;
    private final String routeID;
    private final RoutingManager manager;
    protected static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(
            TheadBasedAddressesAliasUpdater.class);

    /**
     * Creates the task that renames one address.
     *
     * @param manager the manager that sends the update
     * @param routeID the route of the address
     * @param address the address
     */
    public TheadBasedAddressesAliasUpdater(RoutingManager manager, String routeID, Address address) {

        this.manager = manager;
        this.routeID = routeID;
        this.address = address;
    }

    private Address updateAddressFirstName() {
        UUID uuid = UUID.randomUUID();
        address.setAlias(uuid.toString().toUpperCase());
        Address updatedAddress = null;
        try {
            updatedAddress = manager.updateAddressAttribute(routeID, address.getRouteDestinationId(), address);
        } catch (APIException ex) {
            LOGGER.error(ex);
        }
        return updatedAddress;
    }

    @Override
    public Address call() throws Exception {

        Address updatedAddress = updateAddressFirstName();
        return updatedAddress;
    }

    public static void main(String[] args) {

        String routeID = System.getenv("ROUTE_ID");
        if (routeID == null || routeID.isEmpty()) {
            System.err.println("Set ROUTE_ID to the ID of a route of a test account; this example renames its stops.");
            return;
        }
        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Address>> workers = new ArrayList<>();
        try {
            Route route = manager.getRoute(new RoutesRequest().setId(routeID));
            if (route == null || route.getAddresses() == null) {
                System.err.println("The route has no stops");
                executor.shutdown();
                return;
            }
            for (Address address : route.getAddresses()) {
                System.out.println("Current Alias: " + address.getAlias() + " Route Destination ID: "
                        + address.getRouteDestinationId());

                Callable<Address> callableWorker = new TheadBasedAddressesAliasUpdater(manager, routeID, address);
                Future<Address> future = executor.submit(callableWorker);
                workers.add(future);
            }
            executor.shutdown();
            System.out.println("\nUpdating Address in Parallel...\n");
            try {
                if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                    System.err.println("The updates did not finish within 5 minutes");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executor.shutdownNow();
                return;
            }
            for (Future<Address> worker : workers) {
                try {
                    Address updatedAddress = worker.get(60, TimeUnit.MILLISECONDS);
                    if (updatedAddress != null) {
                        System.out.println("Updated Alias: " + updatedAddress.getAlias() + " Route Destination ID: "
                                + updatedAddress.getRouteDestinationId());
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (ExecutionException | TimeoutException ex) {
                    LOGGER.error("Thread Execution Exception. " + ex);
                }
            }
        } catch (APIException ex) {
            executor.shutdownNow();
            Logger.getLogger(TheadBasedAddressesAliasUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
