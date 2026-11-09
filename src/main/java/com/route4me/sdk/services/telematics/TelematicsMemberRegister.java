/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/** The answer of {@link TelematicsManager#registerMember(Integer)}. */
@Data
public class TelematicsMemberRegister {

    /**
     * The telematics API token of the member, which the connection methods of {@link TelematicsManager} take; a
     * credential, so do not log it ({@code api_token}).
     */
    @ToString.Exclude
    @SerializedName("api_token")
    private String apiToken;
    /** When the member was registered ({@code created_at}). */
    @SerializedName("created_at")
    private String createdAt;
    /** When the registration was last updated ({@code updated_at}). */
    @SerializedName("updated_at")
    private String updatedAt;
    /** The ID of the registration ({@code id}). */
    @SerializedName("id")
    private String telematicRegisterID;

}
