/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.geocoding;

public class GeocoderOptions {

    private GeocoderResponseFormat responseFormat = GeocoderResponseFormat.JSON;
    private GeocoderDetails detailed = GeocoderDetails.DETAILED;
    private int maxThreads = Runtime.getRuntime().availableProcessors() + 1;

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

}
