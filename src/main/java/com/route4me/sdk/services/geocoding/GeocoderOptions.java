/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

public class GeocoderOptions {

    /** The default of {@link #getMaxTimeout()}, in milliseconds; the read timeout of a request. */
    public static final long DEFAULT_MAX_TIMEOUT_MS = 30000L;

    private GeocoderResponseFormat responseFormat = GeocoderResponseFormat.JSON;
    private GeocoderDetails detailed = GeocoderDetails.DETAILED;
    private int maxThreads = Runtime.getRuntime().availableProcessors() + 1;
    private int maxRetries = 0;
    private long maxTimeout = DEFAULT_MAX_TIMEOUT_MS;

    public GeocoderOptions() {

    }

    public GeocoderOptions(int maxThreads) {
        this.maxThreads = maxThreads;
    }


    public enum GeocoderDetails {
        SIMPLE("false"),
        DETAILED("true");

        private String value;

        private GeocoderDetails(String value) {
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

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

    public enum GeocoderResponseFormat {
        JSON("json");

        private String value;

        private GeocoderResponseFormat(String value) {
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

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

    /**
     * @return the responseFormat
     */
    public GeocoderResponseFormat getResponseFormat() {
        return responseFormat;
    }

    /**
     * @param responseFormat the responseFormat to set
     */
    public void setResponseFormat(GeocoderResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
    }

    /**
     * @return the detailed
     */
    public GeocoderDetails getDetailed() {
        return detailed;
    }

    /**
     * @param detailed the detailed to set
     */
    public void setDetailed(GeocoderDetails detailed) {
        this.detailed = detailed;
    }

    /**
     * @return the maxThreads
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * @param maxThreads the maxThreads to set
     */
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    /**
     * @return the maximum number of attempts per address; 0 (the default) and 1 both mean
     * one attempt
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * @param maxRetries the maximum number of attempts per address; 0 and 1 both mean one
     *                   attempt
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
