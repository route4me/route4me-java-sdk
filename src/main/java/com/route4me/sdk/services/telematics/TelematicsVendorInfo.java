/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/** A telematics vendor of the catalog, with its features. */
@Data
public class TelematicsVendorInfo {

    /** Unique ID of a telematics vendor ({@code id}). */
    @SerializedName("id")
    private String vendorID;
    /** Vendor name ({@code name}). */
    @SerializedName("name")
    private String name;
    /** Vendor slug ({@code slug}). */
    @SerializedName("slug")
    private String slug;
    /** Vendor description ({@code description}). */
    @SerializedName("description")
    private String description;
    /** URL to the telematics vendor's logo ({@code logo_url}). */
    @SerializedName("logo_url")
    private String logoURL;
    /** Website URL of a telematics vendor ({@code website_url}). */
    @SerializedName("website_url")
    private String websiteURL;
    /** API URL of a telematics vendor ({@code api_docs_url}). */
    @SerializedName("api_docs_url")
    private String apiDocsURL;
    /** Whether, the vendor is or not integrated into the Route4Me system ({@code is_integrated}). */
    @SerializedName("is_integrated")
    private Boolean isIntegrated;
    /** Vendors size. Accepted values: global, regional, local ({@code size}). */
    @SerializedName("size")
    private String size;
    /** The features of the vendor ({@code features}). */
    @SerializedName("features")
    private List<TelematicsVendorFeatures> features;
}
