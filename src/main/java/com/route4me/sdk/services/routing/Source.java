/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import lombok.Data;

/** The source object of an address ({@code source}): its ID and kind. */
@Data
public class Source {

    /** Source ID ({@code source_id}). */
    @SerializedName("source_id")
    @QueryParameter("source_id")
    private Integer sourceID;

    /** Source type (e.g. input_addresses) ({@code source_type}). */
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
