/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Route4Me
 */
@Data
public class TelematicsVendor {

    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("id")
    private String vendorID;
    @QueryParameter("is_enabled")
    @SerializedName("is_enabled")
    private Integer isEnabled;
    @QueryParameter("is_readonly")
    @SerializedName("is_readonly")
    private Integer isReadonly;
    @QueryParameter("name")
    @SerializedName("name")
    private String name;
    @QueryParameter("schema")
    @SerializedName("schema")
    private String schema;
    @QueryParameter("title")
    @SerializedName("title")
    private String title;
    @QueryParameter("icon")
    @SerializedName("icon")
    private String icon;
    @QueryParameter("hint")
    @SerializedName("hint")
    private String hint;
    @QueryParameter("credentials")
    @SerializedName("credentials")
    private List<TelematicsVendorCredentials> credentials;

}
