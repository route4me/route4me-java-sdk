/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

/**
 * The options of {@link GeocodingManager#bulkGeocoder(List, GeocoderOptions)}: the answer format and detail, the
 * number of threads, the attempts per address and the timeout.
 */
public class GeocoderOptions {

    /** The default of {@link #getMaxTimeout()}, in milliseconds; the read timeout of a request. */
    public static final long DEFAULT_MAX_TIMEOUT_MS = 30000L;

    private GeocoderResponseFormat responseFormat = GeocoderResponseFormat.JSON;
    private GeocoderDetails detailed = GeocoderDetails.DETAILED;
    private int maxThreads = Runtime.getRuntime().availableProcessors() + 1;
    private int maxRetries = 0;
    private long maxTimeout = DEFAULT_MAX_TIMEOUT_MS;

    /**
     * Creates the default options: detailed JSON answers, one thread per processor plus one, one attempt, and
     * {@link #DEFAULT_MAX_TIMEOUT_MS}.
     */
    public GeocoderOptions() {

    }

    /**
     * Creates the default options with another thread count.
     *
     * @param maxThreads the maximum number of requests at once
     */
    public GeocoderOptions(int maxThreads) {
        this.maxThreads = maxThreads;
    }


    /** Whether the geocoder answers with every match ({@code detailed=true}) or only coordinates. */
    public enum GeocoderDetails {
        /** Coordinates only ({@code detailed=false}). */
        SIMPLE("false"),
        /** Every match, with its type and confidence ({@code detailed=true}). */
        DETAILED("true");

        private final String value;

        GeocoderDetails(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

    }

    /** The format of the geocoder answer. */
    public enum GeocoderResponseFormat {
        /** JSON ({@code format=json}). */
        JSON("json");

        private final String value;

        GeocoderResponseFormat(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

    }

    /**
     * Returns the format of the answers.
     *
     * @return the format; {@link GeocoderResponseFormat#JSON} by default
     */
    public GeocoderResponseFormat getResponseFormat() {
        return responseFormat;
    }

    /**
     * Sets the format of the answers.
     *
     * @param responseFormat the format
     */
    public void setResponseFormat(GeocoderResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
    }

    /**
     * Returns the detail level of the answers.
     *
     * @return the detail level; {@link GeocoderDetails#DETAILED} by default
     */
    public GeocoderDetails getDetailed() {
        return detailed;
    }

    /**
     * Sets the detail level of the answers.
     *
     * @param detailed the detail level
     */
    public void setDetailed(GeocoderDetails detailed) {
        this.detailed = detailed;
    }

    /**
     * Returns how many requests may run at once.
     *
     * @return the thread count; the number of processors plus one by default
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * Sets how many requests may run at once.
     *
     * @param maxThreads the thread count
     */
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    /**
     * Returns how many times an address may be tried.
     *
     * @return the maximum number of attempts per address; 0 (the default) and 1 both mean one attempt
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Sets how many times an address may be tried.
     *
     * @param maxRetries the maximum number of attempts per address; 0 and 1 both mean one attempt
     */
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * Sets how long {@link GeocodingManager#bulkGeocoder} waits for each round of
     * {@link #getMaxThreads()} addresses before it cancels the rest.
     *
     * @param maxTimeout the time in milliseconds; 0 or less waits until every address is done
     */
    public void setMaxTimeout(long maxTimeout) {
        this.maxTimeout = maxTimeout;
    }

    /**
     * @return how long {@link GeocodingManager#bulkGeocoder} waits for each round of
     * addresses, in milliseconds; {@link #DEFAULT_MAX_TIMEOUT_MS} unless changed
     */
    public long getMaxTimeout() {
        return maxTimeout;
    }



}
