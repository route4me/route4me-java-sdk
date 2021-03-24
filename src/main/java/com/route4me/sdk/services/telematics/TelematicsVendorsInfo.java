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
public class TelematicsVendorsInfo {

    @SerializedName("vendors")
    List<TelematicsVendorInfo> vendors;
    @SerializedName("vendor")
    TelematicsVendorInfo vendor;

}
