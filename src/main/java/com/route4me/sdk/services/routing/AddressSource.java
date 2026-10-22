/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/** Where an address comes from ({@code source}). */
@Data
public class AddressSource {

    /** Where the address comes from ({@code source}). */
    @SerializedName("source")
    @QueryParameter("source")
    private Source source;

    /** Creates an empty source. */
    public AddressSource() {
    }

    /**
     * Creates a source.
     *
     * @param sourceID the ID of the source object
     * @param sourceType the kind of source
     */
    public AddressSource(Integer sourceID, SourceType sourceType) {
        this.source = new Source(sourceID, sourceType.getValue());
    }

}
