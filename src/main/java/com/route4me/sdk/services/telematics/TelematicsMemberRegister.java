/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author Route4Me
 */
@Data
public class TelematicsMemberRegister {

    @ToString.Exclude
    @SerializedName("api_token")
    private String apiToken;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("id")
    private String telematicRegisterID;

}
