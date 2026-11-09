/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

/** The kinds of source of an address; {@code toString()} and {@link #getValue()} give the API value. */
public enum SourceType {
    /** A depot ({@code depots}). */
    DEPOTS("depots"),
    /** An input address ({@code input_addresses}). */
    ADDRESSES("input_addresses");

    private final String value;

    SourceType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * Returns the API value.
     *
     * @return the value, such as {@code depots}
     */
    public String getValue() {
        return value;
    }

}
