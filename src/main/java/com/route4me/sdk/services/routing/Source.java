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
public class Source {

    @SerializedName("source_id")
    @QueryParameter("source_id")
    private Integer sourceID;

    @SerializedName("source_type")
    @QueryParameter("source_type")
    private String sourceType;

    Source() {
    }
    
    Source(Integer sourceID, String sourceType) {
        this.sourceID = sourceID;
        this.sourceType = sourceType;
    }


}
