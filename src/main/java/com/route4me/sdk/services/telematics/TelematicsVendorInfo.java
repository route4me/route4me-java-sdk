/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Route4Me
 */
@Data
public class TelematicsVendorInfo {

    @SerializedName("id")
    private String vendorID;
    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("description")
    private String description;
    @SerializedName("logo_url")
    private String logoURL;
    @SerializedName("website_url")
    private String websiteURL;
    @SerializedName("api_docs_url")
    private String apiDocsURL;
    @SerializedName("is_integrated")
    private Boolean isIntegrated;
    @SerializedName("size")
    private String size;
    @SerializedName("features")
    private List<TelematicsVendorFeatures> features;
}
