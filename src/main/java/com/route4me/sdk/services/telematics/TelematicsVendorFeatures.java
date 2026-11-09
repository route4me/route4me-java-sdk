/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A feature of a telematics vendor. */
@Data
public class TelematicsVendorFeatures {

    /** Feature ID ({@code id}). */
    @SerializedName("id")
    private String id;
    /** Feature name ({@code name}). */
    @SerializedName("name")
    private String name;
    /** Feature slug ({@code slug}). */
    @SerializedName("slug")
    private String slug;
    /** Feature group ({@code feature_group}). */
    @SerializedName("feature_group")
    private String featureGroup;

}
