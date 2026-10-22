/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/** The answer of the deprecated vendor methods of {@link TelematicsManager}: one vendor or a list. */
@Data
public class TelematicsVendorsInfo {

    /** The vendors, in the answer for a list ({@code vendors}). */
    @SerializedName("vendors")
    private List<TelematicsVendorInfo> vendors;
    /** The vendor, in the answer for one vendor ({@code vendor}). */
    @SerializedName("vendor")
    private TelematicsVendorInfo vendor;

}
