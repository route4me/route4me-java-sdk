/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/**
 *
 * @author Route4Me
 */
@Data
public class AddressSource {


    @SerializedName("source")
    @QueryParameter("source")
    private Source source;

    public AddressSource() {
    }

    public AddressSource(Integer sourceID, SourceType sourceType) {
        this.source = new Source(sourceID, sourceType.getValue());
    }

    
}
