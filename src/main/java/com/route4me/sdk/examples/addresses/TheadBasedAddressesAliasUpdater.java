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
 *
 * @author route4me
 */
public class TheadBasedAddressesAliasUpdater implements Callable {

    private final Address address;
    private final String routeID;
    private final RoutingManager manager;
    protected static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TheadBasedAddressesAliasUpdater.class);

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
            logger.error(ex);
        }
        return updatedAddress;
    }

    @Override
    public Object call() throws Exception {

        Address updatedAddress = updateAddressFirstName();
        return updatedAddress;
    }

    public static void main(String[] args) {

        String apiKey = System.getenv("R4M_API_KEY");
        RoutingManager manager = new RoutingManager(apiKey);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        String routeID = "5ED2D6B7E7FCB926D6B164A530DA4224";
        List<Future<Address>> workers = new ArrayList<>();
        try {
            Route route = manager.getRoute(new RoutesRequest().setId(routeID));
            for (Address address : route.getAddresses()) {
                System.out.println("Current Alias: " + address.getAlias() + " Route Destination ID: " + address.getRouteDestinationId());

                Callable callableWorker = new TheadBasedAddressesAliasUpdater(manager, routeID, address);
                Future<Address> future = executor.submit(callableWorker);
                workers.add(future);
            }
            executor.shutdown();
            System.out.println("\nUpdating Address in Parallel...\n");
            while (!executor.isTerminated()) {
            }
            for (Future<Address> worker : workers) {
                try {
                    Address updatedAddress = worker.get(60, TimeUnit.MILLISECONDS);
                    System.out.println("Updated Alias: " + updatedAddress.getAlias() + " Route Destination ID: " + updatedAddress.getRouteDestinationId());
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    logger.error("Thread Execution Exception. " + ex);
                }
            }
        } catch (APIException ex) {
            Logger.getLogger(TheadBasedAddressesAliasUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
