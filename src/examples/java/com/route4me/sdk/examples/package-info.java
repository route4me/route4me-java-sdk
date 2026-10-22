/**
 * Runnable examples of the Route4Me Java SDK: route optimizations of the {@code RoutingManager} here, and one
 * sub-package per API. The examples read the API key from the {@code R4M_API_KEY} environment variable and send
 * real requests when you run them; the build compiles them but never runs them.
 *
 * <p>The examples call the live API, and some of them create, change or delete data of the account. Run them
 * against a test account only. The examples that change or delete existing data take their targets from
 * environment variables, such as {@code ROUTE_ID}, {@code ROUTE_DESTINATION_ID} or {@code CONTACT_ID}, and stop
 * with a message when they are not set; none of them picks "the newest route" or "the first contact".
 */
package com.route4me.sdk.examples;
