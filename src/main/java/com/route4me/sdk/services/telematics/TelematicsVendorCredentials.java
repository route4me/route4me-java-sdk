/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A credential field a telematics vendor needs. */
@Data
public class TelematicsVendorCredentials {

    /** The name of the credential field, such as {@code username} ({@code name}). */
    @SerializedName("name")
    private String name;
    /** Whether the credential is used ({@code is_enabled}). */
    @SerializedName("is_enabled")
    private Boolean isEnabled;
    /** The default value of the credential field ({@code defaut}). */
    @SerializedName("defaut")
    private String defaut;

}
