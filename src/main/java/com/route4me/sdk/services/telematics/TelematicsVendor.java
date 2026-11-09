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

/** A telematics vendor, as {@link TelematicsManager#getTelematicsVendor(String)} returns it. */
@Data
public class TelematicsVendor {

    /** When the vendor was added ({@code created_at}). */
    @SerializedName("created_at")
    private String createdAt;
    /** When the vendor was last updated ({@code updated_at}). */
    @SerializedName("updated_at")
    private String updatedAt;
    /** Unique ID of a telematics vendor ({@code id}). */
    @SerializedName("id")
    private String vendorID;
    /** Whether the vendor is enabled ({@code is_enabled}). */
    @QueryParameter("is_enabled")
    @SerializedName("is_enabled")
    private Integer isEnabled;
    /** Whether connections to the vendor are read-only ({@code is_readonly}). */
    @QueryParameter("is_readonly")
    @SerializedName("is_readonly")
    private Integer isReadonly;
    /** Vendor name ({@code name}). */
    @QueryParameter("name")
    @SerializedName("name")
    private String name;
    /** The schema of the data the vendor exchanges ({@code schema}). */
    @QueryParameter("schema")
    @SerializedName("schema")
    private String schema;
    /** The display name of the vendor ({@code title}). */
    @QueryParameter("title")
    @SerializedName("title")
    private String title;
    /** The icon of the vendor ({@code icon}). */
    @QueryParameter("icon")
    @SerializedName("icon")
    private String icon;
    /** A hint about the credentials the vendor needs ({@code hint}). */
    @QueryParameter("hint")
    @SerializedName("hint")
    private String hint;
    /** Credentials a connection to this vendor needs ({@code credentials}). */
    @QueryParameter("credentials")
    @SerializedName("credentials")
    private List<TelematicsVendorCredentials> credentials;

}
